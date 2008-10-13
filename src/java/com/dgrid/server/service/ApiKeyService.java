package com.dgrid.server.service;

import com.dgrid.gen.InvalidApiKey;
import com.dgrid.server.model.ApiKey;

public interface ApiKeyService {

	public static final String NAME = "apiKeyService";

	public ApiKey getApiKey(String apiKey) throws InvalidApiKey;
}
