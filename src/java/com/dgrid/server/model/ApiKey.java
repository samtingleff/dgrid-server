package com.dgrid.server.model;

public class ApiKey {

	private int id;

	private long timeCreated;

	private String key;

	public ApiKey() {
	}

	public ApiKey(int id, long timeCreated, String key) {
		this.id = id;
		this.timeCreated = timeCreated;
		this.key = key;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
