package com.iot.app.spark.entity;

import java.io.Serializable;

/**
 * Class to represent total_accidents db table
 * 
 *
 */
public class TotalTrafficAccidentData implements Serializable{

	private String cityId;
	private String day;
	private long accidents;
	private String daytime;
	private float longitude;
	private float latitude;
	
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
	
	public float getLatitude(){
		return latitude;
	}

	public void setLatitude(float latitude){
		this.latitude = latitude;
	}

	public float getLongitude(){
		return longitude;
	}
	
	public void setLongitude(float longitude){
		this.longitude = longitude;
	}
	
}
