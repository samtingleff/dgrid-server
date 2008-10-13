package com.dgrid.server.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class DGridServerApplication extends WebApplication {

	public void init() {
		super.init();
		addComponentInstantiationListener(new SpringComponentInjector(this));
		mountBookmarkablePage("hosts", HostsPage.class);
		mountBookmarkablePage("settings", SystemSettingsPage.class);
		mountBookmarkablePage("host", HostSettingsPage.class);
		mountBookmarkablePage("host/delete", HostDeletePage.class);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return HostsPage.class;
	}

}
