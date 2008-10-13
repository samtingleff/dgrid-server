package com.dgrid.server.service.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;

import com.dgrid.dao.ObjectQueryDAO;
import com.dgrid.errors.TransportException;
import com.dgrid.gen.Host;
import com.dgrid.gen.InvalidApiKey;
import com.dgrid.gen.InvalidHost;
import com.dgrid.gen.InvalidJobId;
import com.dgrid.gen.InvalidJobletId;
import com.dgrid.gen.Job;
import com.dgrid.gen.JobService;
import com.dgrid.gen.Joblet;
import com.dgrid.gen.JobletResult;
import com.dgrid.gen.NoHostAvailable;
import com.dgrid.gen.NoWorkAvailable;
import com.dgrid.server.model.ApiKey;
import com.dgrid.server.service.ApiKeyService;
import com.dgrid.service.DGridTransport;
import com.facebook.thrift.TException;

public class DGridEndpointImpl implements JobService.Iface {

	private Log log = LogFactory.getLog(getClass());

	private ObjectQueryDAO queryDAO;

	private ApiKeyService apiKeyService;

	private DGridTransport transport;

	private Random random = new SecureRandom();

	public void setObjectQueryDAO(ObjectQueryDAO dao) {
		this.queryDAO = dao;
	}

	public void setApiKeyService(ApiKeyService service) {
		this.apiKeyService = service;
	}

	public void setTransport(DGridTransport transport) {
		this.transport = transport;
	}

	public void init() {
		log.trace("init()");
	}

	public void completeJoblet(String apiKey, int hostid, int jobletId,
			JobletResult result, String logMessage) throws InvalidApiKey,
			InvalidJobletId, TException {
		log.trace("completeJoblet()");
		validateApiKey(apiKey);
		try {
			transport.completeJoblet(jobletId, result, logMessage);
		} catch (TransportException e) {
			log.error("TransportException in completeJoblet()", e);
			throw new TException(e);
		}
	}

	public Host getHost(String apiKey, String hostname) throws InvalidApiKey,
			InvalidHost, TException {
		log.trace("getHost()");
		validateApiKey(apiKey);
		try {
			return transport.getHostByName(hostname);
		} catch (TransportException e) {
			log.error("TransportException in getHost()", e);
			throw new TException(e);
		}
	}

	public String getHostSetting(String apiKey, int hostid, String name,
			String defaultValue) throws InvalidApiKey, InvalidHost, TException {
		log.trace("getHostSetting()");
		validateApiKey(apiKey);
		try {
			return transport.getHostSetting(hostid, name, defaultValue);
		} catch (TransportException e) {
			log.error("TransportException in getHostSetting()", e);
			throw new TException(e);
		}
	}

	public Job getJob(String apiKey, int jobId) throws InvalidApiKey,
			InvalidJobId, TException {
		log.trace("getJob()");
		validateApiKey(apiKey);
		try {
			return transport.getJob(jobId);
		} catch (TransportException e) {
			log.error("TransportException in getJob()", e);
			throw new TException(e);
		}
	}

	public int getJobletQueueSize(String apiKey) throws InvalidApiKey,
			TException {
		log.trace("getJobletQueueSize()");
		validateApiKey(apiKey);
		try {
			return transport.getJobletQueueSize();
		} catch (TransportException e) {
			log.error("TransportException in getJobletQueueSize()", e);
			throw new TException(e);
		}
	}

	public JobletResult getJobletResult(String apiKey, int jobletId)
			throws InvalidApiKey, InvalidJobletId, TException {
		log.trace("getJobletResult()");
		validateApiKey(apiKey);
		try {
			return transport.getJobletResult(jobletId);
		} catch (TransportException e) {
			log.error("TransportException in getJobletResult()", e);
			throw new TException(e);
		}
	}

	public List<JobletResult> getResults(String apiKey, int jobId)
			throws InvalidApiKey, InvalidJobId, TException {
		log.trace("getResults()");
		validateApiKey(apiKey);
		try {
			return transport.getResults(jobId);
		} catch (TransportException e) {
			log.error("TransportException in getResults()", e);
			throw new TException(e);
		}
	}

	public String getSetting(String apiKey, String name, String defaultValue)
			throws InvalidApiKey, TException {
		log.trace("getSetting()");
		validateApiKey(apiKey);
		try {
			return transport.getSetting(name, defaultValue);
		} catch (TransportException e) {
			log.error("TransportException in getSetting()", e);
			throw new TException(e);
		}
	}

	public Host getSyncJobServiceHost(String apiKey) throws InvalidApiKey,
			NoHostAvailable, TException {
		log.trace("getSyncJobServiceHost()");
		validateApiKey(apiKey);
		// want to pick a random host
		Criteria crit = queryDAO.createCriteria(Host.class);
		int size = crit.list().size();
		int hostNumber = random.nextInt(size);
		Host host = (Host) crit.list().get(hostNumber);
		return host;
	}

	public Joblet getWork(String apiKey, int hostid) throws InvalidApiKey,
			NoWorkAvailable, InvalidHost, TException {
		log.trace("getWork()");
		validateApiKey(apiKey);
		try {
			return transport.getWork();
		} catch (TransportException e) {
			log.error("TransportException in getWork()", e);
			throw new TException(e);
		}
	}

	public void log(String apiKey, int hostid, int jobletId, int status,
			String message) throws InvalidApiKey, InvalidJobletId, TException {
		log.trace("log()");
		validateApiKey(apiKey);
		try {
			transport.log(jobletId, status, message);
		} catch (TransportException e) {
			log.error("TransportException in log()", e);
			throw new TException(e);
		}
	}

	public Host registerHost(String apiKey, String hostname)
			throws InvalidApiKey, TException {
		log.trace("registerHost()");
		validateApiKey(apiKey);
		try {
			return transport.registerHost(hostname);
		} catch (TransportException e) {
			log.error("TransportException in registerHost()", e);
			throw new TException(e);
		} catch (InvalidHost e) {
			log.error("InvalidHost in registerHost()", e);
			throw new TException(e);
		}
	}

	public void releaseJoblet(String apiKey, int hostid, int jobletId)
			throws InvalidApiKey, InvalidJobletId, TException {
		log.trace("releaseJoblet()");
		validateApiKey(apiKey);
		try {
			transport.releaseJoblet(jobletId);
		} catch (TransportException e) {
			log.error("TransportException in releaseJoblet()", e);
			throw new TException(e);
		}
	}

	public void setHostFacts(String apiKey, int hostid,
			Map<String, String> facts) throws InvalidApiKey, InvalidHost,
			TException {
		log.trace("setHostFacts()");
		validateApiKey(apiKey);
		try {
			transport.setHostFacts(hostid, facts);
		} catch (TransportException e) {
			log.error("TransportException in setHostFacts()", e);
			throw new TException(e);
		}
	}

	public int submitJob(String apiKey, Job job) throws InvalidApiKey,
			TException {
		log.trace("submitJob()");
		validateApiKey(apiKey);
		try {
			return transport.submitJob(job);
		} catch (TransportException e) {
			log.error("TransportException in submitJob()", e);
			throw new TException(e);
		}
	}

	public int submitJoblet(String apiKey, Joblet joblet, int jobId,
			int callbackType, String callbackAddress, String callbackContent)
			throws InvalidApiKey, InvalidJobId, TException {
		log.trace("submitJoblet()");
		validateApiKey(apiKey);
		try {
			return transport.submitJoblet(joblet, jobId, callbackType,
					callbackAddress, callbackContent);
		} catch (TransportException e) {
			log.error("TransportException in submitJoblet()", e);
			throw new TException(e);
		}
	}

	private ApiKey validateApiKey(String apiKey) throws InvalidApiKey {
		log.trace("validateApiKey()");
		ApiKey key = apiKeyService.getApiKey(apiKey);
		return key;
	}
}
