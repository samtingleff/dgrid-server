package com.dgrid.server.test;

import java.util.HashMap;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;

import com.dgrid.gen.JOB_STATUS;
import com.dgrid.gen.Job;
import com.dgrid.gen.Joblet;
import com.dgrid.server.service.GenericDAOService;
import com.dgrid.server.service.JobletSerializer;

public class JmsListenerTestCase extends BaseTestCase {

	public void testJmsListener() throws Exception {
		GenericDAOService dao = (GenericDAOService) super.ctx
				.getBean(GenericDAOService.NAME);
		String queueName = "dgrid.joblets";
		int jobPreCount = dao.count(Job.class);
		int jobletPreCount = dao.count(Joblet.class);
		sendTestMessage(queueName);
		Thread.sleep(2000);
		int jobPostCount = dao.count(Job.class);
		int jobletPostCount = dao.count(Joblet.class);
		assertTrue((jobPreCount + 1) == jobPostCount);
		assertTrue((jobletPreCount + 1) == jobletPostCount);

		// delete it
		List<Job> jobs = dao.list(Job.class, 0, 1, "timeCreated", false);
		assertTrue(jobs.size() >= 1);
		Job j = jobs.get(0);
		for (Joblet joblet : j.getJoblets()) {
			dao.delete(Joblet.class, joblet.getId());
		}
		dao.delete(Job.class, j.getId());
	}

	private void sendTestMessage(String queueName) throws JMSException {
		JmsTemplate jmsTemplate = (JmsTemplate) super.ctx
				.getBean("jmsTemplate");
		JobletSerializer js = (JobletSerializer) super.ctx
				.getBean("jsonJobletSerializer");

		Joblet joblet = new Joblet(0, System.currentTimeMillis(), 0, 0, "sam",
				1, "system", "test", new HashMap<String, String>(),
				"echo hello", JOB_STATUS.RECEIVED);
		QueueConnectionFactory qcf = (QueueConnectionFactory) jmsTemplate
				.getConnectionFactory();
		QueueConnection qc = null;
		try {
			qc = qcf.createQueueConnection();
			qc.start();
			QueueSession qs = qc.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Queue queue = qs.createQueue(queueName);
			TextMessage msg = qs.createTextMessage();
			msg.setText(js.serialize(joblet));
			MessageProducer producer = qs.createProducer(queue);
			producer.send(msg);
			qc.stop();
		} finally {
			qc.close();
		}
	}
}
