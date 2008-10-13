package com.dgrid.server.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;

import com.dgrid.gen.Constants;
import com.dgrid.gen.JOB_CALLBACK_TYPES;
import com.dgrid.gen.JobService;
import com.dgrid.gen.Joblet;
import com.dgrid.server.service.DGridListenerService;
import com.dgrid.server.service.JobletSerializer;
import com.dgrid.server.service.SettingsService;

public class JmsQueueBridge implements DGridListenerService, MessageListener {
	private Log log = LogFactory.getLog(getClass());

	private JmsTemplate jmsTemplate;

	private JobService.Iface handler;

	private SettingsService settings;

	private JobletSerializer jobletSerializer;

	private QueueConnection queueConnection;

	private String queueName = "dgrid.joblets";

	private String apiKey = "changeme";

	private boolean enabled = false;

	public void setJmsTemplate(JmsTemplate template) {
		this.jmsTemplate = template;
	}

	public void setHandler(JobService.Iface handler) {
		this.handler = handler;
	}

	public void setSettingsService(SettingsService service) {
		this.settings = service;
	}

	public void setJobletSerializer(JobletSerializer js) {
		this.jobletSerializer = js;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void init() throws JMSException {
		log.trace("init()");
		this.queueName = settings.getSystemSetting("jmsBridge.jobletQueueName",
				queueName);
		this.enabled = settings.getSystemSettingBoolean("jmsBridge.enable",
				false);
		if (!enabled) {
			log.info("Setting jmsBridge.enable is false, not starting");
		} else {
			run();
		}
	}

	public void destroy() {
		log.trace("destroy()");
		if (!enabled)
			stop();
	}

	public void run() {
		log.trace("run()");
		try {
			QueueConnectionFactory qcf = (QueueConnectionFactory) jmsTemplate
					.getConnectionFactory();
			queueConnection = qcf.createQueueConnection();
			queueConnection.start();
			QueueSession qs = queueConnection.createQueueSession(false,
					Session.CLIENT_ACKNOWLEDGE);
			Queue queue = qs.createQueue(queueName);
			QueueReceiver qr = qs.createReceiver(queue);
			qr.setMessageListener(this);
		} catch (JMSException e) {
			log.error("JMSException in run(), will not read from jms queue", e);
		}
	}

	public void stop() {
		log.trace("stop()");
		try {
			queueConnection.stop();
		} catch (Exception e) {
			log.warn("Exception calling queueConnection.stop()", e);
		} finally {
			closeQueueConnection(queueConnection);
		}
	}

	public void onMessage(Message msg) {
		log.trace("onMessage()");
		try {
			if ((msg != null) && (msg instanceof TextMessage)) {
				TextMessage tm = (TextMessage) msg;
				String text = tm.getText();
				Joblet joblet = jobletSerializer.deserialize(text);
				handler.submitJoblet(apiKey, joblet, joblet.getJobId(),
						JOB_CALLBACK_TYPES.NONE, null, null);
			} else {
				log.warn(String.format(
						"Unknown message (%1$s) received. Ignoring.", msg));
			}
		} catch (Exception e) {
			log.error(String.format("Exception reading joblet from msg (%1$s)",
					msg), e);
		} finally {
			try {
				msg.acknowledge();
			} catch (JMSException e) {
				log.error(String.format(
						"Exception calling acknowledge() on msg (%1$s)", msg),
						e);
			}
		}
	}

	private void closeQueueConnection(QueueConnection qc) {
		try {
			qc.close();
		} catch (Exception e) {
			log.warn("Could not close QueueConnection:", e);
		}
	}
}
