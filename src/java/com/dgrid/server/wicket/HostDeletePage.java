package com.dgrid.server.wicket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

import com.dgrid.gen.Host;
import com.dgrid.server.service.GenericDAOService;

public class HostDeletePage extends WebPage {
	private Log log = LogFactory.getLog(getClass());

	@SpringBean(name = GenericDAOService.NAME)
	private GenericDAOService genericDAO;

	public HostDeletePage(PageParameters parameters) {
		log.trace("HostDeletePage()");
		try {
			int hostid = parameters.getInt("id");
			genericDAO.delete(Host.class, hostid);
		} catch (StringValueConversionException e) {
			log.warn("StringValueConversionException in HostDeletePage()");
		}
		setResponsePage(HostsPage.class);
	}
}
