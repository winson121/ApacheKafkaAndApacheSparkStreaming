package com.iot.app.spark.processor;

import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.Optional;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.State;
import org.apache.spark.streaming.StateSpec;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaMapWithStateDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
//import com.google.common.base.Optional;

import com.iot.app.spark.entity.TotalTrafficAccidentData;
import com.iot.app.spark.vo.AggregateKeyAcc;
import com.iot.app.spark.vo.IoTData2;

import scala.Tuple2;

/**
 * Class to process IoT data stream and to produce traffic data details.
 * 
 * @author abaghel
 *
 */
public class IoTDaytimeProcessor {
	private static final Logger logger = Logger.getLogger(IoTDaytimeProcessor.class);

	/**
	 * Method to get total traffic counts of different type of vehicles for each route.
	 * 
	 * @param filteredIotDataStream IoT data stream
	 */
	public void processDayTimeAccidentData(JavaDStream<IoTData2> filteredIotDataStream) {

		// We need to get count of Accidents group by city, day and daytime
		JavaPairDStream<AggregateKeyAcc, Long> countDStreamPair = filteredIotDataStream
				.mapToPair(iot -> new Tuple2<>(new AggregateKeyAcc(iot.getCity(), iot.getDay(),iot.getTimeOfDay(),iot.getLongitude(),iot.getLatitude()), 1L))
				.reduceByKey((a, b) -> a + b);
		
		// Need to keep state for total count
		JavaMapWithStateDStream<AggregateKeyAcc, Long, Long, Tuple2<AggregateKeyAcc, Long>> countDStreamWithStatePair = countDStreamPair
				.mapWithState(StateSpec.function(totalSumFunc).timeout(Durations.seconds(3600)));//maintain state for one hour

		// Transform to dstream of daytime event
		JavaDStream<Tuple2<AggregateKeyAcc, Long>> countDStream = countDStreamWithStatePair.map(tuple2 -> tuple2);
		JavaDStream<TotalTrafficAccidentData> trafficDStream = countDStream.map(totalTrafficAccidentsDataFunc);

		// Map Cassandra table column
		Map<String, String> columnNameMappings = new HashMap<String, String>();
		columnNameMappings.put("cityId", "cityid");
		columnNameMappings.put("day", "day");
		columnNameMappings.put("accidents", "accidents");
		columnNameMappings.put("daytime", "daytime");
		columnNameMappings.put("latitude","latitude");
		columnNameMappings.put("longitude","longitude");

		// call CassandraStreamingJavaUtil function to save in DB
		javaFunctions(trafficDStream).writerBuilder("traffickeyspace", "traffic_accidents",
				CassandraJavaUtil.mapToRow(TotalTrafficAccidentData.class, columnNameMappings)).saveToCassandra();
	}
	
	//Function to get running sum by maintaining the state
	private static final Function3<AggregateKeyAcc, Optional<Long>, State<Long>,Tuple2<AggregateKeyAcc,Long>> totalSumFunc = (key,currentSum,state) -> {
        long totalSum = currentSum.or(0L) + (state.exists() ? state.get() : 0);
        Tuple2<AggregateKeyAcc, Long> total = new Tuple2<>(key, totalSum);
        state.update(totalSum);
        return total;
    };
    
    //Function to create TotalTrafficAccidentData object from IoT data
    private static final Function<Tuple2<AggregateKeyAcc, Long>, TotalTrafficAccidentData> totalTrafficAccidentsDataFunc = (tuple -> {
		logger.debug("Total Count : " + "key " + tuple._1().getCityId() + "-" + tuple._1().getDay() + " value "+ tuple._2() + tuple._1().getDaytime() + " value "+ tuple._2());
		TotalTrafficAccidentData trafficData = new TotalTrafficAccidentData();
		trafficData.setCityId(tuple._1().getCityId());
		trafficData.setDay(tuple._1().getDay());
		trafficData.setAccidents(tuple._2());
		trafficData.setDaytime(tuple._1().getDaytime());
		trafficData.setLongitude(tuple._1().getLongitude());
		trafficData.setLatitude(tuple._1().getLatitude());
		return trafficData;
	});
}
