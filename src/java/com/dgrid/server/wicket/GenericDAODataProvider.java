package com.dgrid.server.wicket;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import com.dgrid.server.service.GenericDAOService;

public class GenericDAODataProvider<V> implements IDataProvider<V> {

	private static final long serialVersionUID = -8215456559195216571L;

	private Log log = LogFactory.getLog(getClass());

	private GenericDAOService genericDAOService;

	private Class<V> cls;

	public GenericDAODataProvider(GenericDAOService service, Class<V> cls) {
		this.genericDAOService = service;
		this.cls = cls;
	}

	public IModel<V> model(V object) {
		return new GenericIModel<V>(object);
	}

	@SuppressWarnings("unchecked")
	public Iterator<V> iterator(int first, int count) {
		log.trace("iterator()");
		List<V> list = genericDAOService.list(cls, first, count, null, true);
		return list.iterator();
	}

	public int size() {
		log.trace("size()");
		return genericDAOService.count(cls);
	}

	public void detach() {
		log.trace("detach()");
	}

	private static class GenericIModel<V> implements IModel<V> {

		private static final long serialVersionUID = 7075361325556311158L;

		private V object;

		public GenericIModel(V object) {
			this.object = object;
		}

		public V getObject() {
			return object;
		}

		public void setObject(V object) {
			this.object = object;
		}

		public void detach() {
			this.object = null;
		}
	}
}
