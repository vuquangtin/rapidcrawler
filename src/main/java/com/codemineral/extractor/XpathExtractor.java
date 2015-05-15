package com.codemineral.extractor;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.xsoup.Xsoup;
import us.codecraft.xsoup.xevaluator.XPathEvaluator;

import com.codemineral.exception.XpathParsingException;

public class XpathExtractor implements Extractor {
	private static Logger logger = LoggerFactory
			.getLogger(XpathExtractor.class);

	private String xpathPat;
	private XPathEvaluator evaluator;

	public XpathExtractor(String xpathPat) {
		this.xpathPat = xpathPat;
		try {
			evaluator = Xsoup.compile(xpathPat);
		} catch (Exception e) {
			logger.warn("xpath compiling error", e);
			throw new XpathParsingException(
					"xpath compiling error " + xpathPat, e);
		}
	}

	@Override
	public String extract(String raw) {
		String result = StringUtils.EMPTY;
		try {
			Document doc = Jsoup.parse(raw);
			result = Xsoup.compile(xpathPat).evaluate(doc).get();
		} catch (Exception e) {
			logger.warn("xpath extractor error", e);
		}
		return result;
	}

	@Override
	public List<String> extractAll(String raw) {
		List<String> list = new LinkedList<String>();
		try {
			Document doc = Jsoup.parse(raw);
			list = evaluator.evaluate(doc).list();
		} catch (Exception e) {
			logger.warn("xpath extractor error", e);
		}
		return list;
	}

	@Override
	public List<String> extracAll(List<String> rawList) {
		List<String> list = new LinkedList<String>();
		try {
			for (String raw : rawList) {
				String result = extract(raw);
				if (!result.equals(StringUtils.EMPTY)) {
					list.add(result);
				}
			}
		} catch (Exception e) {
			logger.warn("xpath extractor error", e);
		}
		return list;
	}

}
