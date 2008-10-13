package com.dgrid.server.service.impl;

import java.io.Serializable;
import java.util.List;

import com.dgrid.dao.GenericDAO;
import com.dgrid.server.service.GenericDAOService;

public class GenericDAOServiceImpl implements GenericDAOService {

	private GenericDAO dao;

	public void setGenericDAO(GenericDAO dao) {
		this.dao = dao;
	}

	public Object create(Object object) {
		return dao.create(object);
	}

	public Object read(Class cls, Serializable id) {
		return dao.read(cls, id);
	}

	public Object update(Object object) {
		return dao.update(object);
	}

	public Object delete(Class cls, Serializable id) {
		return dao.delete(cls, id);
	}

	public int count(Class cls) {
		return dao.count(cls);
	}

	public List list(Class cls, int offset, int max, String orderProperty,
			boolean asc) {
		return dao.list(cls, offset, max, orderProperty, asc);
	}

}
