package com.example.miniapppointsofinterest.model.object;

public class LocationBoundary {
	
	private Double lat;
	private Double lng;
	
	public LocationBoundary() {}

	public LocationBoundary(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lng=" + lng + "]";
	}
	
}
