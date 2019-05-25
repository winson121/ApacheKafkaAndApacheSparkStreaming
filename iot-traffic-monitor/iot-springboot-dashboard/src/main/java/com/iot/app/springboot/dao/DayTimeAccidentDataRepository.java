package com.iot.app.springboot.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.iot.app.springboot.dao.entity.DayTimeAccidentData;

/**
 * DAO class for total_traffic 
 * 
 * @author abaghel
 *
 */
@Repository
public interface DayTimeAccidentDataRepository extends CassandraRepository<DayTimeAccidentData>{

	 @Query("SELECT * FROM traffickeyspace.traffic_accidents")
	 Iterable<DayTimeAccidentData> findTrafficDataByCity(String cityId);
}
