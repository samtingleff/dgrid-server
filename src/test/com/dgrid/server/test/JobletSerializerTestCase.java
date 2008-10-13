package com.dgrid.server.test;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import com.dgrid.gen.JOB_STATUS;
import com.dgrid.gen.Joblet;
import com.dgrid.server.service.JobletSerializer;

public class JobletSerializerTestCase extends BaseTestCase {

	public void testJsonSerializer() throws Exception {
		JobletSerializer js = (JobletSerializer) super.ctx
				.getBean("jsonJobletSerializer");
		Map<String, String> params = new HashMap<String, String>();
		Joblet joblet1 = new Joblet(0, System.currentTimeMillis(), 0, 0, "sam",
				1, "system", "test joblet", params, "echo hello",
				JOB_STATUS.RECEIVED);
		JSON json = JSONSerializer.toJSON(joblet1);
		String text = json.toString();

		Joblet joblet2 = js.deserialize(text);
		assertEquals(joblet2.getTimeCreated(), joblet1.getTimeCreated());
		assertEquals(joblet2.getSubmitter(), joblet1.getSubmitter());
		assertEquals(joblet2.getContent(), joblet1.getContent());

		// test simple json
		text = "{timeCreated:10, submitter:'jeff', jobletType:'groovy', content:'abc', parameters:{script:'123'}}";
		Joblet joblet3 = js.deserialize(text);
		assertEquals(joblet3.getTimeCreated(), 10l);
		assertEquals(joblet3.getSubmitter(), "jeff");
		assertEquals(joblet3.getJobletType(), "groovy");
		assertEquals(joblet3.getContent(), "abc");
		assertTrue(joblet3.getParameters() != null);
		assertEquals(joblet3.getParameters().size(), 1);
		assertEquals(joblet3.getParameters().get("script"), "123");
	}
}
