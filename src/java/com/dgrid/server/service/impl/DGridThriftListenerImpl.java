package com.dgrid.server.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dgrid.gen.Constants;
import com.dgrid.gen.JobService;
import com.dgrid.server.service.DGridListenerService;
import com.dgrid.server.service.SettingsService;
import com.facebook.thrift.protocol.TBinaryProtocol;
import com.facebook.thrift.protocol.TProtocolFactory;
import com.facebook.thrift.server.TServer;
import com.facebook.thrift.server.TThreadPoolServer;
import com.facebook.thrift.transport.TServerSocket;
import com.facebook.thrift.transport.TServerTransport;
import com.facebook.thrift.transport.TTransportException;
import com.facebook.thrift.transport.TTransportFactory;

public class DGridThriftListenerImpl implements DGridListenerService, Runnable {

	private Log log = LogFactory.getLog(getClass());

	private JobService.Iface handler;

	private SettingsService settings;

	private TServer server;

	private int port = Constants.DEFAULT_PORT;

	private boolean enabled = true;

	public void setHandler(JobService.Iface handler) {
		this.handler = handler;
	}

	public void setSettingsService(SettingsService service) {
		this.settings = service;
	}

	public void init() {
		log.trace("init()");
		this.port = settings.getSystemSettingInt("thriftServer.port",
				Constants.DEFAULT_PORT);
		this.enabled = settings.getSystemSettingBoolean("thriftServer.enable",
				true);
		if (!enabled) {
			log.info("Setting thriftServer.enable is false, not starting");
		} else {
			Thread t = new Thread(this, "Thrift server");
			t.start();
		}
	}

	public void destroy() {
		log.trace("destroy()");
		stop();
	}

	public void run() {
		log.trace("run()");
		try {
			JobService.Processor processor = new JobService.Processor(handler);
			TServerTransport serverTransport = new TServerSocket(port);
			TTransportFactory tfactory = new TTransportFactory();
			TProtocolFactory pfactory = new TBinaryProtocol.Factory();
			this.server = new TThreadPoolServer(processor, serverTransport,
					tfactory, pfactory);
			this.server.serve();
		} catch (TTransportException e) {
			log.error("TTransportException in run()", e);
		}
	}

	public void stop() {
		log.trace("stop()");
		try {
			if (this.server != null)
				this.server.stop();
		} catch (Exception e) {
			log.warn("Exception in stop()", e);
		}
	}
}
