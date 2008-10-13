package com.dgrid.server.wicket;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.dgrid.gen.Host;
import com.dgrid.gen.HostState;
import com.dgrid.server.service.GenericDAOService;
import com.dgrid.service.DGridSyncJobService;

public class HostsPage extends WebPage {

	private Log log = LogFactory.getLog(getClass());

	@SpringBean(name = GenericDAOService.NAME)
	private GenericDAOService genericDAOService;

	@SpringBean(name = DGridSyncJobService.NAME)
	private DGridSyncJobService syncJobService;

	private DataView<Host> hostDataView;

	@SuppressWarnings("unchecked")
	public HostsPage() {
		log.trace("HostsPage()");

		hostDataView = new DataView<Host>("hosts",
				new GenericDAODataProvider<Host>(genericDAOService, Host.class)) {
			private static final long serialVersionUID = -2140712654641366554L;

			@Override
			protected void populateItem(Item<Host> item) {
				final Host host = item.getModelObject();
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("id", host.getId());
				PageParameters params = new PageParameters(parameterMap);
				HostState state = getState(host.getHostname());

				item.add(new Label("hostname", host.getHostname()));
				item.add(new BookmarkablePageLink("hostSettingsLink",
						HostSettingsPage.class, params));
				item.add(new BookmarkablePageLink("hostDeleteLink",
						HostDeletePage.class, params));

				String contextPath = getRequest()
						.getRelativePathPrefixToContextRoot();
				System.err.println("context: " + contextPath);
				// TODO: need a relative path in this images

				Image stateImage = (state.getVmUptime() > 0l) ? new StaticImage(
						"img-status", "/dgrid/static/images/accept-16x16.png")
						: new StaticImage("img-status",
								"/dgrid/static/images/exclamation-16x16.png");

				item.add(stateImage);
				item.add(new StaticImage("img-restart",
						"/dgrid/static/images/play-16x16.png"));
				item.add(new StaticImage("img-stop",
						"/dgrid/static/images/stop-16x16.png"));
				item.add(new Label("activeTasks", Integer.toString(state
						.getActiveTasks())));
				item.add(new Label("freeMemory", Long.toString(state
						.getFreeMemory())));
				item.add(new Label("loadAverage", Double.toString(state
						.getLoadAverage())));
			}

		};
		hostDataView.setItemsPerPage(20);
		add(hostDataView);
		add(new PagingNavigator("navigator", hostDataView));
		add(new NavomaticBorder("navomaticBorder"));
	}

	private HostState getState(String hostname) {
		HostState state = null;
		try {
			state = syncJobService.status(hostname);
		} catch (Exception e) {
			log.warn(String.format("Exception calling status() on host (%1$s)",
					hostname));
			state = new HostState(0l, 0.0d, 0l, 0l, 0, 0);
		}
		return state;
	}
}
