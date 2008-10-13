package com.dgrid.server.service.impl;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.dgrid.gen.Joblet;
import com.dgrid.server.service.JobletSerializer;

public class JsonJobletSerializer implements JobletSerializer {

	public Joblet deserialize(String text) {
		JSONObject json = JSONObject.fromObject(text);
		Joblet joblet = (Joblet) JSONObject.toBean(json, Joblet.class);
		return joblet;
	}

	public String serialize(Joblet joblet) {
		JSON json = JSONSerializer.toJSON(joblet);
		return json.toString();
	}

}
