package com.dgrid.server.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.dgrid.dao.ObjectQueryDAO;
import com.dgrid.gen.InvalidApiKey;
import com.dgrid.server.model.ApiKey;
import com.dgrid.server.service.ApiKeyService;

public class ApiKeyServiceImpl implements ApiKeyService {

	private Log log = LogFactory.getLog(getClass());

	private ObjectQueryDAO dao;

	public void setObjectQueryDAO(ObjectQueryDAO dao) {
		this.dao = dao;
	}

	public ApiKey getApiKey(String apiKey) throws InvalidApiKey {
		log.trace("getApiKey()");
		Criteria crit = dao.createCriteria(ApiKey.class);
		crit.add(Restrictions.eq("key", apiKey));
		ApiKey key = (ApiKey) crit.uniqueResult();
		if (key == null) {
			log.warn(String.format("Rejecting invalid api key (%1$s)", apiKey));
			throw new InvalidApiKey();
		}
		return key;
	}

}
