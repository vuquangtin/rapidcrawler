package com.codemineral.downloader;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codemineral.core.Site;
import com.codemineral.http.Page;
import com.codemineral.http.Request;
import com.codemineral.http.Request.Method;

public class HttpDownloader implements Downloader {
	private static Logger logger = LoggerFactory.getLogger(Downloader.class);

	private int threadNum = 1;
	private PoolingHttpClientConnectionManager cm;
	private CloseableHttpClient client;
	private Site site;

	public HttpDownloader(int threadNum, Site site) {
		this.threadNum = threadNum;
		this.site = site;
		initConfig();
	}

	@Override
	public Page download(Request request, Site site) {
		logger.info("downloading url ---- " + request.getUrl());
		HttpUriRequest req = buildRequest(request);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(req);
			HttpEntity entity = response.getEntity();
			byte[] content = IOUtils.toByteArray(entity.getContent());
			Page page = new Page();
			page.setRawText(new String(content, site.getCharset()));
			return page;
		} catch (IOException e) {
			logger.warn("downloading error", e);
		}
		return null;
	}

	@Override
	public void setThreadNum(int num) {
		this.threadNum = num;
	}

	private HttpUriRequest buildRequest(Request req) {
		HttpUriRequest request = null;
		if (req.getMethod() == Method.Get) {
			request = new HttpGet(req.getUrl());
		} else {
			request = new HttpPost(req.getUrl());
		}
		/*
		 * 先没有添加cookie和http头
		 * FIX ME!!!
		 */
		return request;
	}

	private void initConfig() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http",
						PlainConnectionSocketFactory.getSocketFactory())
				.register("https",
						SSLConnectionSocketFactory.getSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(registry);
		cm.setMaxTotal(threadNum);
		RequestConfig config = RequestConfig.custom().setRedirectsEnabled(true)
				.setConnectTimeout(site.getConnectionTimeout())
				.setSocketTimeout(site.getSocketTimeout())
				.setCookieSpec(CookieSpecs.BEST_MATCH)
				.setCircularRedirectsAllowed(false).build();
		client = HttpClients.custom().setConnectionManager(cm)
				.setUserAgent(site.getUserAgent())
				.addInterceptorFirst(new HttpRequestInterceptor() {

					@Override
					public void process(HttpRequest request, HttpContext context)
							throws HttpException, IOException {
						if (site.isUseGzip()
								&& !request.containsHeader("Accept-Encoding")) {
							request.addHeader("Accept-Encoding", "gzip");
						}
						if (!request.containsHeader("Host")) {
							request.addHeader("Host", site.getHost());
						}
					}
				}).setDefaultRequestConfig(config).build();
	}
}
