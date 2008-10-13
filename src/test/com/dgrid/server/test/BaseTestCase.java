package com.dgrid.server.test;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dgrid.server.model.ApiKey;
import com.dgrid.server.service.GenericDAOService;

import junit.framework.TestCase;

public abstract class BaseTestCase extends TestCase {
	protected AbstractApplicationContext ctx;

	protected String apiKey;

	protected ApiKey apiKeyObject;

	public void setUp() {
		String[] paths = new String[] {
				"/com/dgrid/server/spring/applicationContext-hibernate.xml",
				"/com/dgrid/server/spring/applicationContext-webapp.xml" };
		ctx = new ClassPathXmlApplicationContext(paths);
		this.apiKeyObject = createApiKey("changeme");
		this.apiKey = apiKeyObject.getKey();
	}

	public void tearDown() {
		ctx.registerShutdownHook();
		deleteApiKey(apiKeyObject.getId());
	}

	protected ApiKey createApiKey(String key) {
		ApiKey apiKey = new ApiKey(0, System.currentTimeMillis(), key);
		GenericDAOService service = (GenericDAOService) ctx
				.getBean(GenericDAOService.NAME);
		apiKey = (ApiKey) service.create(apiKey);
		return apiKey;
	}

	protected ApiKey deleteApiKey(int id) {
		GenericDAOService service = (GenericDAOService) ctx
				.getBean(GenericDAOService.NAME);
		ApiKey key = (ApiKey) service.delete(ApiKey.class, id);
		return key;
	}
}
