package com.iot.app.spark.vo;

import java.io.Serializable;

/**
 * Class to represent the IoT vehicle data.
 * 
 * @author abaghel
 *
 */
public class IoTData2 implements Serializable{
    
    private String eventId;
	private String city;
	private String timeOfDay;
	private String day;
	private Boolean accident;
	private Float longitude;
	private Float latitude;
	public IoTData2(){
		
	}
	
	public IoTData2(String city, String day, String timeOfDay,String eventId, Boolean accident, Float longitude, Float latitude) {
        super();
        this.eventId = eventId;
        this.city = city;
        this.day = day;
				this.timeOfDay = timeOfDay;
				this.accident = accident;
				this.longitude = longitude;
				this.latitude = latitude;
	}

	public String getCity() {
		return city;
	}

	public String getDay() {
		return day;
	}

	public String getTimeOfDay() {
		return timeOfDay;
    }
    
  public String getEventId() {
		return eventId;
	}
	public Boolean getAccident() {
		return accident;
	}

	public Float getLongitude(){
		return longitude;
	}

	public Float getLatitude(){
		return latitude;
	}
}