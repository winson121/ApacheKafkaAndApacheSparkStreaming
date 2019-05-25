package com.iot.app.spark.vo;

import java.io.Serializable;

/**
 * Key class for calculation
 * 
 *
 */
public class AggregateKeyAcc implements Serializable {
	
	private String cityId;
	private String day;
    private String daytime;
	private Float longitude;
	private Float latitude;

	public AggregateKeyAcc(String cityId, String day, String daytime,Float longitude, Float latitude) {
		super();
		this.cityId = cityId;
		this.day = day;
		this.daytime = daytime;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getCityId() {
		return cityId;
	}

	public String getDay() {
		return day;
    }
    
    public String getDaytime(){
        return daytime;
    }

	public Float getLongitude(){
		return longitude;
	}

	public Float getLatitude(){
		return latitude;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
        result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((daytime == null) ? 0 : daytime.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj !=null && obj instanceof AggregateKeyAcc){
			AggregateKeyAcc other = (AggregateKeyAcc)obj;
			if(other.getCityId() != null && other.getDay() != null && other.getDaytime() != null && other.getLongitude() != null && other.getLatitude() != null){
				if((other.getCityId().equals(this.cityId)) && (other.getDay().equals(this.day)) && (other.getDaytime().equals(this.daytime)) 
				&& (other.getLongitude().equals(this.longitude)) && other.getLatitude().equals(this.latitude)){
					return true;
				} 
			}
		}
		return false;
	}
	

}
