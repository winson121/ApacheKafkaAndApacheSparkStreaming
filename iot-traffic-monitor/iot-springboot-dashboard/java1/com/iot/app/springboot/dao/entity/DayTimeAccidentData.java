package com.iot.app.springboot.dao.entity;

import java.io.Serializable;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Class to represent total_accidents db table
 * 
 *
 */
@Table("traffic_accidents")
public class DayTimeAccidentData implements Serializable{
    @PrimaryKeyColumn(name = "cityid",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private String cityId;
    @PrimaryKeyColumn(name = "day", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String day;
    @Column(value="accidents")
    private long accidents;
    @Column(value="daytime")
	private String daytime;
	
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public long getAccidents() {
		return accidents;
	}
	public void setAccidents(Long accidents) {
		this.accidents = accidents;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getDaytime() {
		return daytime;
	}

	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}
	
	@Override
	public String toString() {
		return "TrafficData [cityId=" + cityId + ", day=" + day + ", accidents=" + accidents
				+ ", daytime=" + daytime + "]";
	}
}
