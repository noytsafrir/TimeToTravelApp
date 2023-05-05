package com.example.miniapppointsofinterest.model.object;

public class ObjectId {
	private String superapp;
	private String internalObjectId;
	
	
	public ObjectId() {}


	public ObjectId(String superapp, String internalObjectId) {
		this.superapp = superapp;
		this.internalObjectId = internalObjectId;
	}


	public String getSuperapp() {
		return superapp;
	}


	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}


	public String getInternalObjectId() {
		return internalObjectId;
	}


	public void setInternalObjectId(String internalObjectId) {
		this.internalObjectId = internalObjectId;
	}


	@Override
	public String toString() {
		return "ObjectId [superapp=" + superapp + ", internalObjectId=" + internalObjectId + "]";
	}
}
