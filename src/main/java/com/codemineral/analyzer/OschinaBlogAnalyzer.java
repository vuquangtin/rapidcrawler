package com.codemineral.analyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codemineral.http.Page;
import com.codemineral.store.ResultBundle;

public class OschinaBlogAnalyzer implements Analyzer {
	private static Logger logger = LoggerFactory.getLogger(Analyzer.class);

	@Override
	public ResultBundle process(Page page) {
		ResultBundle bundle = new ResultBundle();
		bundle.setRequest(page.getRequest());
		try {
			Document doc = Jsoup.parse(page.getRawText());
			Element titleElement = doc.select("head title").first();
			String title = titleElement.text();
			bundle.put("title", title);
			//			bundle.put("content", page.getRawText());
			bundle.put("url", page.getRequest().getUrl());
		} catch (Exception e) {
			logger.warn("页面解析错误！ url:" + page.getRequest().getUrl(), e);
		}
		return bundle;
	}

}
