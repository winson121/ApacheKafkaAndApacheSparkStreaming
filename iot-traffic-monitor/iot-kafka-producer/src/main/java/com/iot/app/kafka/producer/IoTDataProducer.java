package com.iot.app.kafka.producer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import com.iot.app.kafka.util.PropertyFileReader;
import com.iot.app.kafka.vo.IoTData1;
import com.iot.app.kafka.vo.IoTData2;

import org.apache.log4j.Logger;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * IoT data event producer class which uses Kafka producer for events. 
 * 
 * @author abaghel
 *
 */
public class IoTDataProducer {
	
	private static final Logger logger = Logger.getLogger(IoTDataProducer.class);

	public static void main(String[] args) throws Exception {
		//read config file
		String route = args[0];
		Properties prop = PropertyFileReader.readPropertyFile();
		Properties prop2 = PropertyFileReader.readPropertyFile();			
		String zookeeper = prop.getProperty("com.iot.app.kafka.zookeeper");
		String brokerList = prop.getProperty("com.iot.app.kafka.brokerlist");
		String zookeeper2 = prop2.getProperty("com.iot.app.kafka.zookeeper");
		String brokerList2 = prop2.getProperty("com.iot.app.kafka.brokerlist");
		String topic = prop.getProperty("com.iot.app.kafka.topic");
		String topic2 = prop.getProperty("com.iot.app.kafka.topic2");
		logger.info("Using Zookeeper=" + zookeeper + " ,Broker-list=" + brokerList + " and topic " + topic);
		//logger.info("Using Zookeeper=" + zookeeper2 + " ,Broker-list=" + brokerList2 + " and topic2 " + topic2);

		// set producer properties
		
		Properties properties = new Properties();
		properties.put("zookeeper.connect", zookeeper);
		properties.put("metadata.broker.list", brokerList);
		properties.put("request.required.acks", "1");
		properties.put("serializer.class", "com.iot.app.kafka.util.IoTDataEncoder");
		
		// set producer properties2
		Properties properties2 = new Properties();
		properties2.put("zookeeper.connect", zookeeper2);
		properties2.put("metadata.broker.list", brokerList2);
		properties2.put("request.required.acks", "1");
		properties2.put("serializer.class", "com.iot.app.kafka.util.IoTDataEncoder");
		
		//generate event
		Producer<String, IoTData1> producer = new Producer<String, IoTData1>(new ProducerConfig(properties));
		Producer<String, IoTData2> producer2 = new Producer<String, IoTData2>(new ProducerConfig(properties2));
		IoTDataProducer iotProducer = new IoTDataProducer();
		iotProducer.generateIoTEvent(producer,topic,producer2,topic2,route);		
	}


	/**
	 * Method runs in while loop and generates random IoT data in JSON with below format. 
	 * 
	 * {"vehicleId":"52f08f03-cd14-411a-8aef-ba87c9a99997","vehicleType":"Public Transport","routeId":"route-43","latitude":",-85.583435","longitude":"38.892395","timestamp":1465471124373,"speed":80.0,"fuelLevel":28.0}
	 * 
	 * @throws InterruptedException 
	 * 
	 * 
	 */
	private void generateIoTEvent(Producer<String, IoTData1> producer, String topic,Producer<String, IoTData2> producer2, String topic2,String route) throws InterruptedException {
		Random rand = new Random();
		// generate event in loop
		while (true) {
			List<IoTData1> eventList = trafficEvents(rand,route);
			List<IoTData2> eventList2 = dayEvents(rand);
			Collections.shuffle(eventList);// shuffle for random events
			Collections.shuffle(eventList2);
			for (int i=0; i<eventList.size(); i++) {
				KeyedMessage<String, IoTData1> data = new KeyedMessage<String, IoTData1>(topic, eventList.get(i));
				KeyedMessage<String, IoTData2> data2 = new KeyedMessage<String, IoTData2>(topic2, eventList2.get(i));
				producer.send(data);
				producer2.send(data2);
				Thread.sleep(rand.nextInt(3000 - 1000) + 1000);//random delay of 1 to 3 seconds
			}
		}
	}

	private List<IoTData1> trafficEvents(Random rand, String route){
		//String[] routes = {"Route-37", "Route-43", "Route-82"};
		Map<String,String> routeList = new HashMap<String,String>();
		routeList.put("Route-37","Route-37");
		routeList.put("Route-43","Route-43");
		routeList.put("Route-82","Route-82");
		List<String> vehicleTypeList = Arrays.asList(new String[]{"Large Truck", "Small Truck", "Private Car", "Bus", "Taxi"});
		logger.info("Sending events");

		List<IoTData1> eventList = new ArrayList<IoTData1>();
		for (int i = 0; i < 100; i++) {// create 100 vehicles
			String vehicleId = UUID.randomUUID().toString();
			String vehicleType = vehicleTypeList.get(rand.nextInt(5));
			String routeId = routeList.get(route);
			Date timestamp = new Date();
			double speed = rand.nextInt(100 - 20) + 20;// random speed between 20 to 100
			double fuelLevel = rand.nextInt(40 - 10) + 10;
			for (int j = 0; j < 5; j++) {// Add 5 events for each vehicle
				String coords = getCoordinates(routeId);
				String latitude = coords.substring(0, coords.indexOf(","));
				String longitude = coords.substring(coords.indexOf(",") + 1, coords.length());
				IoTData1 event = new IoTData1(vehicleId, vehicleType, routeId, latitude, longitude, timestamp, speed,fuelLevel);
				eventList.add(event);
			}
		}
		return eventList;
	}
	private List<IoTData2> dayEvents(Random rand) throws InterruptedException {
		List<String> dayTimeList = Arrays.asList(new String[]{"morning", "day", "night"});
		List<String> dayList = Arrays.asList(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday","Friday", "Saturday", "Sunday"});
		List<String> cityList = Arrays.asList(new String[]{"Petaling Jaya", "Puchong"});
		List<Boolean> accidents = Arrays.asList(new Boolean[]{true,false});
		logger.info("Sending events2");
		// generate event in loop
		
		List<IoTData2> eventList = new ArrayList<IoTData2>();
		for (int i = 0; i < 100; i++) {// create 100 daytime time event
			String eventId = UUID.randomUUID().toString();
			String day = dayList.get(rand.nextInt(7));
			Boolean accident = accidents.get(rand.nextInt(2));
			String city = cityList.get(rand.nextInt(2));
			for(int j=0;j < 5;j++){
				Float[] coords = getCityCoordinate(city);
				Float latitude = coords[0];
				Float longitude = coords[1];
				String timeOfDay = dayTimeList.get(rand.nextInt(3));
				IoTData2 event = new IoTData2(city, day, timeOfDay, eventId,accident, longitude, latitude);
				eventList.add(event);
			}	
		}
		return eventList;
	}
	
	private Float[] getCityCoordinate(String city){
		Random rand = new Random();
		float latPrefix = 0;
		float longPrefix = 0;
		List<Float> suffixLocation = Arrays.asList(new Float[]{0.004f, 0.007f, 0.001f, 0.006f, 0.002f, 0.003f}); 
		if (city.equals("Puchong")) {
			latPrefix = 50.02f;
			longPrefix = 101.61f;
		} else if (city.equals("Petaling Jaya")) {
			latPrefix = 45.10f;
			longPrefix = 110.64f;
		}
		Float latitude = latPrefix + suffixLocation.get(rand.nextInt(6));
		Float longitude = longPrefix + suffixLocation.get(rand.nextInt(6));
		Float[] coordinate = new Float[]{longitude,latitude};
		return coordinate;
	}
	//Method to generate random latitud1e and longitude for routes
	private String  getCoordinates(String routeId) {
		Random rand = new Random();
		int latPrefix = 0;
		int longPrefix = -0;
		if (routeId.equals("Route-37")) {
			latPrefix = 33;
			longPrefix = -96;
		} else if (routeId.equals("Route-82")) {
			latPrefix = 34;
			longPrefix = -97;
		} else if (routeId.equals("Route-43")) {
			latPrefix = 35;
			longPrefix = -98;
		} 
		Float lati = latPrefix + rand.nextFloat();
		Float longi = longPrefix + rand.nextFloat();
		return lati + "," + longi;
	}
}
