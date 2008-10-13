package com.dgrid.server.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.dgrid.dao.GenericDAO;
import com.dgrid.dao.ObjectQueryDAO;
import com.dgrid.dao.model.HostSetting;
import com.dgrid.dao.model.SystemSetting;
import com.dgrid.gen.Host;
import com.dgrid.server.service.SettingsService;

public class SettingsServiceImpl implements SettingsService {

	private Log log = LogFactory.getLog(getClass());

	private GenericDAO dao;

	private ObjectQueryDAO queryDAO;

	public void setGenericDAO(GenericDAO dao) {
		this.dao = dao;
	}

	public void setObjectQueryDAO(ObjectQueryDAO dao) {
		this.queryDAO = dao;
	}

	@SuppressWarnings("unchecked")
	public List<SystemSetting> listSystemSettings() {
		log.trace("listSystemSettings()");
		return dao.list(SystemSetting.class, 0, -1, "name", true);
	}

	public String getSystemSetting(String name, String defaultValue) {
		log.trace("getSystemSetting()");
		SystemSetting ss = getOrCreateSystemSetting(name, defaultValue);
		return ss.getValue();
	}

	public boolean getSystemSettingBoolean(String name, boolean defaultValue) {
		log.trace("getSystemSettingBoolean()");
		SystemSetting ss = getOrCreateSystemSetting(name, Boolean
				.toString(defaultValue));
		return Boolean.parseBoolean(ss.getValue());
	}

	public int getSystemSettingInt(String name, int defaultValue) {
		log.trace("getSystemSettingInt()");
		SystemSetting ss = getOrCreateSystemSetting(name, Integer
				.toString(defaultValue));
		return Integer.parseInt(ss.getValue());
	}

	public SystemSetting updateSystemSetting(String name, String value) {
		log.trace("updateSystemSetting()");
		SystemSetting ss = getOrCreateSystemSetting(name, value);
		if (!ss.getValue().equals(value)) {
			ss.setValue(value);
			ss = (SystemSetting) dao.update(ss);
		}
		return ss;
	}

	@SuppressWarnings("unchecked")
	public List<HostSetting> listHostSettings() {
		log.trace("listHostSettings()");
		Criteria crit = queryDAO.createCriteria(HostSetting.class);
		crit.addOrder(Order.asc("name"));
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<HostSetting> listHostSettings(int hostid) {
		log.trace("listHostSettings()");
		Criteria crit = queryDAO.createCriteria(HostSetting.class);
		crit.createCriteria("host").add(Restrictions.eq("id", hostid));
		crit.addOrder(Order.asc("name"));
		return crit.list();
	}

	public String getHostSetting(int hostid, String name, String defaultValue) {
		log.trace("getHostSetting()");
		HostSetting hs = getOrCreateHostSetting(hostid, name, defaultValue);
		return hs.getValue();
	}

	public boolean getHostSettingBoolean(int hostid, String name,
			boolean defaultValue) {
		log.trace("getHostSettingBoolean()");
		HostSetting hs = getOrCreateHostSetting(hostid, name, Boolean
				.toString(defaultValue));
		return Boolean.parseBoolean(hs.getValue());
	}

	public int getHostSettingInt(int hostid, String name, int defaultValue) {
		log.trace("getHostSettingInt()");
		HostSetting hs = getOrCreateHostSetting(hostid, name, Integer
				.toString(defaultValue));
		return Integer.parseInt(hs.getValue());
	}

	public HostSetting updateHostSetting(int hostid, String name, String value) {
		log.trace("updateSystemSetting()");
		HostSetting hs = getOrCreateHostSetting(hostid, name, value);
		if (!hs.getValue().equals(value)) {
			hs.setValue(value);
			hs = (HostSetting) dao.update(hs);
		}
		return hs;
	}

	private SystemSetting getOrCreateSystemSetting(String name,
			String defaultValue) {
		log.trace("getOrCreateSystemSetting()");
		Criteria crit = queryDAO.createCriteria(SystemSetting.class);
		crit.add(Restrictions.eq("name", name));
		SystemSetting ss = (SystemSetting) crit.uniqueResult();
		if (ss == null) {
			ss = new SystemSetting(0, System.currentTimeMillis(), name,
					defaultValue, null);
			ss = (SystemSetting) dao.create(ss);
		}
		return ss;
	}

	private HostSetting getOrCreateHostSetting(int hostid, String name,
			String defaultValue) {
		log.trace("getOrCreateSystemSetting()");
		Criteria crit = queryDAO.createCriteria(HostSetting.class);
		crit.createCriteria("host").add(Restrictions.eq("id", hostid));
		crit.add(Restrictions.eq("name", name));
		HostSetting hs = (HostSetting) crit.uniqueResult();
		if (hs == null) {
			Host host = (Host) dao.read(Host.class, hostid);
			hs = new HostSetting(0, System.currentTimeMillis(), name,
					defaultValue, null, host);
			hs = (HostSetting) dao.create(hs);
		}
		return hs;
	}
}
