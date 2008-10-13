package com.dgrid.server.wicket;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

import com.dgrid.dao.model.HostSetting;
import com.dgrid.server.service.SettingsService;

public class HostSettingsPage extends WebPage {
	private Log log = LogFactory.getLog(getClass());

	@SpringBean(name = SettingsService.NAME)
	private SettingsService settings;

	private ListView<HostSetting> settingListView;

	private List<HostSetting> settingList;

	@SuppressWarnings("unchecked")
	public HostSettingsPage(PageParameters parameters) {
		log.trace("HostSettingsPage()");
		int hostid = 0;
		try {
			hostid = parameters.getInt("id");
		} catch (StringValueConversionException e) {
		}
		settingList = (hostid == 0) ? settings.listHostSettings() : settings
				.listHostSettings(hostid);
		settingListView = new ListView<HostSetting>("settings", settingList) {
			private static final long serialVersionUID = -8923661042160126228L;

			@Override
			public void populateItem(final ListItem<HostSetting> listItem) {
				final HostSetting setting = (HostSetting) listItem
						.getModelObject();
				listItem.add(new Label("hostname", setting.getHost()
						.getHostname()));
				listItem.add(new Label("name", setting.getName()));
				listItem
						.add(new Label("description", setting.getDescription()));
				listItem.add(new AjaxEditableLabel<String>("value",
						new HostSettingIModel(settings, setting)));
			}
		};
		settingListView.setReuseItems(false);
		add(settingListView);
		add(new NavomaticBorder("navomaticBorder"));
	}

	private static class HostSettingIModel implements IModel<String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 756496789666587763L;

		private SettingsService settingsService;

		private HostSetting setting;

		public HostSettingIModel(SettingsService service, HostSetting setting) {
			this.settingsService = service;
			this.setting = setting;
		}

		public String getObject() {
			return setting.getValue();
		}

		public void setObject(String value) {
			this.setting = settingsService.updateHostSetting(setting.getHost()
					.getId(), setting.getName(), value);
		}

		public void detach() {
		}

	}
}
