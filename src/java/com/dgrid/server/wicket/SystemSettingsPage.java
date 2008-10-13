package com.dgrid.server.wicket;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.dgrid.dao.model.SystemSetting;
import com.dgrid.server.service.SettingsService;

public class SystemSettingsPage extends WebPage {
	private Log log = LogFactory.getLog(getClass());

	@SpringBean(name = SettingsService.NAME)
	private SettingsService settings;

	private ListView<SystemSetting> settingListView;

	private List<SystemSetting> settingList;

	public SystemSettingsPage() {
		log.trace("SystemSettingsPage()");
		settingList = settings.listSystemSettings();
		settingListView = new ListView<SystemSetting>("settings", settingList) {
			private static final long serialVersionUID = -8923661042160126228L;

			@Override
			public void populateItem(final ListItem<SystemSetting> listItem) {
				final SystemSetting setting = (SystemSetting) listItem
						.getModelObject();
				listItem.add(new Label("name", setting.getName()));
				listItem
						.add(new Label("description", setting.getDescription()));
				listItem.add(new AjaxEditableLabel<String>("value",
						new SystemSettingIModel(settings, setting)));
			}
		};
		settingListView.setReuseItems(false);
		add(settingListView);
		add(new NavomaticBorder("navomaticBorder"));
	}

	private static class SystemSettingIModel implements IModel<String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4849882112164360861L;

		private SettingsService settingsService;

		private SystemSetting setting;

		public SystemSettingIModel(SettingsService service,
				SystemSetting setting) {
			this.settingsService = service;
			this.setting = setting;
		}

		public String getObject() {
			return setting.getValue();
		}

		public void setObject(String value) {
			this.setting = settingsService.updateSystemSetting(setting
					.getName(), value);
		}

		public void detach() {
		}

	}
}
