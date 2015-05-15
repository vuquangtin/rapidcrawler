package com.codemineral.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codemineral.analyzer.OschinaBlogAnalyzer;
import com.codemineral.core.Crawler;
import com.codemineral.core.Site;

public class TestCrawler {
	private List<String> startUrls = new ArrayList<String>();
	private Logger logger = LoggerFactory.getLogger(TestCrawler.class);

	@Before
	public void setup() {
		startUrls.add("http://my.oschina.net/u/567839/blog/413072");
		startUrls.add("http://my.oschina.net/u/567839/blog/385009");
	}

	public void test() {
		Site site = Site.create();
		site.setHost("my.oschina.net");
		site.setConnectionTimeout(5000);
		site.addAll(startUrls);
		Crawler crawler = Crawler.create();
		crawler.setSite(site);
		crawler.setAnalyzer(new OschinaBlogAnalyzer());
		crawler.start();
	}

	public void testLogConfigure() {
		logger.info("info");
		logger.warn("warn");
		logger.debug("debug");
		logger.error("error", new NullPointerException("null"));

	}
}
