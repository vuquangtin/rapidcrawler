package com.codemineral.store;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.codemineral.http.Request;

public class ResultBundle {
	private Request request;

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	private Map<Object, Object> map = new HashMap<Object, Object>();

	public void put(String name, String value) {
		map.put(name, value);
	}

	public String getString(String name) {
		return (String) map.get(name);
	}

	public Iterator<Object> keyIterator() {
		return map.keySet().iterator();
	}

	public Object get(Object key) {
		return map.get(key);
	}
}
