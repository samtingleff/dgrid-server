package com.dgrid.server.service;

import java.util.List;

import com.dgrid.dao.model.HostSetting;
import com.dgrid.dao.model.SystemSetting;

public interface SettingsService {
	public static final String NAME = "settingsService";

	public List<SystemSetting> listSystemSettings();

	public String getSystemSetting(String name, String defaultValue);

	public int getSystemSettingInt(String name, int defaultValue);

	public boolean getSystemSettingBoolean(String name, boolean defaultValue);

	public SystemSetting updateSystemSetting(String name, String value);

	public List<HostSetting> listHostSettings();

	public List<HostSetting> listHostSettings(int hostid);

	public String getHostSetting(int hostid, String name, String defaultValue);

	public int getHostSettingInt(int hostid, String name, int defaultValue);

	public boolean getHostSettingBoolean(int hostid, String name,
			boolean defaultValue);

	public HostSetting updateHostSetting(int hostid, String name, String value);
}
