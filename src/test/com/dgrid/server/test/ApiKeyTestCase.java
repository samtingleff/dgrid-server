package com.dgrid.server.test;

import com.dgrid.gen.InvalidApiKey;
import com.dgrid.server.model.ApiKey;
import com.dgrid.server.service.ApiKeyService;
import com.dgrid.server.service.GenericDAOService;

public class ApiKeyTestCase extends BaseTestCase {

	public void testApiKey() throws Exception {
		GenericDAOService dao = (GenericDAOService) ctx
				.getBean(GenericDAOService.NAME);
		ApiKeyService keyService = (ApiKeyService) ctx
				.getBean(ApiKeyService.NAME);

		ApiKey key = new ApiKey(0, System.currentTimeMillis(), "abc");
		key = (ApiKey) dao.create(key);
		ApiKey key2 = keyService.getApiKey(key.getKey());
		assertEquals(key2.getKey(), key.getKey());

		// test an invalid key
		boolean caught = false;
		try {
			ApiKey key3 = keyService.getApiKey("123");
		} catch (InvalidApiKey e) {
			caught = true;
		}
		assertTrue(caught);
		dao.delete(ApiKey.class, key2.getId());
	}
}
