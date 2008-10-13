package com.dgrid.server.wicket;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.border.BoxBorder;

public class NavomaticBorder extends Border {

	/**
	 * 
	 */
	private static final long serialVersionUID = -495231425259720556L;

	public NavomaticBorder(final String id) {
		super(id);
		add(new BoxBorder("navigationBorder"));
		add(new BoxBorder("bodyBorder"));
	}

}
