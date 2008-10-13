package com.dgrid.server.wicket;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class StaticImage extends Image {

	private static final long serialVersionUID = 1300297003689240556L;

	public StaticImage(String cls, String src) {
		super(cls);
		this.add(new StaticImageModifier(src));
		this.setOutputMarkupId(true);
	}

	private static class StaticImageModifier extends AttributeModifier {
		private static final long serialVersionUID = 1L;

		public StaticImageModifier(String src) {
			super("src", true, new StaticImageReadOnlyModel(src));
		}
	}

	@SuppressWarnings("unchecked")
	private static class StaticImageReadOnlyModel extends AbstractReadOnlyModel {
		private static final long serialVersionUID = 1L;

		private String src;

		public StaticImageReadOnlyModel(String src) {
			this.src = src;
		}

		@Override
		public final Object getObject() {
			return src;
		}
	}
}
