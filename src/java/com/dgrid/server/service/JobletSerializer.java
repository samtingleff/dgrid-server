package com.dgrid.server.service;

import com.dgrid.gen.Joblet;

public interface JobletSerializer {

	public String serialize(Joblet joblet);

	public Joblet deserialize(String text);
}
