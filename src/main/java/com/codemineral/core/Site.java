package com.codemineral.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.codemineral.http.Request;
import com.codemineral.utils.UserAgent;

public class Site {
	private String charset = "utf-8";
	private String host;
	private List<Request> startRequests = new ArrayList<Request>();
	private int retryNum = 0;
	private long sleepTime = 3000;
	private int socketTimeout = 3000;
	private int connectionTimeout = 3000;
	private String userAgent = UserAgent.CHROME_UA;
	private boolean useGzip = true;

	private Site() {
	}

	public boolean isUseGzip() {
		return useGzip;
	}

	public void setUseGzip(boolean useGzip) {
		this.useGzip = useGzip;
	}

	public void setStartRequests(List<Request> startRequests) {
		this.startRequests = startRequests;
	}

	public static Site create() {
		return new Site();
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public List<Request> getStartRequests() {
		return startRequests;
	}

	public void addStartUrl(String url) {
		startRequests.add(new Request(url));
	}

	public void addAll(Collection<String> urls) {
		for (String url : urls) {
			addStartUrl(url);
		}
	}
}
