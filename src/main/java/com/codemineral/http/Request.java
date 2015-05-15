package com.codemineral.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class Request {
	private String url;
	private Map<String, String> httpHeaders = new LinkedHashMap<String, String>();
	private Map<String, String> cookieStore = new LinkedHashMap<String, String>();
	private Method method = Method.Get;

	public Request(String url) {
		this.url = url;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(Map<String, String> cookieStore) {
		this.cookieStore = cookieStore;
	}

	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public void addHeader(String name, String val) {
		httpHeaders.put(name, val);
	}

	public static enum Method {
		Get, Post
	}
}
