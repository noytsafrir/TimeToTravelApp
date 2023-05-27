package com.example.miniapppointsofinterest.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Point {

	public enum TYPE{
		FOOD,
		ENTERTAINMENT,
		HISTORICAL,
		NATURE,
		OTHER
	}

	private String privateOrPublic;
	private double rating;
	private String image;
	private TYPE type;
	private Map<String, Object> details;

	public Point() {}

	public Point(TYPE type, String privateOrPublic, double rating, String image) {
		this.type = type;
		this.privateOrPublic = privateOrPublic;
		this.rating = rating;
		this.image = image;
		this.details = new HashMap<>();
	}

	public Point(TYPE type, String privateOrPublic, double rating, String image, Map<String, Object> details) {
		this.type = type;
		this.privateOrPublic = privateOrPublic;
		this.rating = rating;
		this.image = image;
		this.details = details;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String isPrivateOrPublic() {
		return privateOrPublic;
	}

	public void setPrivateOrPublic(String privateOrPublic) {
		this.privateOrPublic = privateOrPublic;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}
}
