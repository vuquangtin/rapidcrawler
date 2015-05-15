package com.codemineral.extractor;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CssExtractor implements Extractor {
	private static Logger logger = LoggerFactory.getLogger(CssExtractor.class);

	private String cssPat;

	private CssExtractor(String cssPat) {
		this.cssPat = cssPat;
	}

	public static Extractor build(String cssPat) {
		return new CssExtractor(cssPat);
	}

	@Override
	public String extract(String raw) {
		Document doc = Jsoup.parse(raw);
		Elements allElements = css(doc, raw);
		if (allElements == null || allElements.size() == 0)
			return StringUtils.EMPTY;
		else {
			return allElements.first().html();
		}
	}

	@Override
	public List<String> extracAll(List<String> rawList) {
		List<String> list = new LinkedList<String>();
		for (String raw : rawList) {
			String result = extract(raw);
			if (!result.equals(StringUtils.EMPTY)) {
				list.add(result);
			}
		}
		return list;
	}

	@Override
	public List<String> extractAll(String raw) {
		List<String> list = new LinkedList<String>();
		Document doc = Jsoup.parse(raw);
		Elements allElements = css(doc, raw);
		if (allElements == null || allElements.size() == 0)
			return list;
		else {
			for (int i = 0; i < allElements.size(); i++) {
				list.add(allElements.get(i).html());
			}
			return list;
		}
	}

	private Elements css(Document doc, String raw) {
		try {
			return doc.select(cssPat);
		} catch (Exception e) {
			logger.warn("parsing error - css selector: " + cssPat, e);
		}
		return null;
	}

	public String getCssPat() {
		return cssPat;
	}

	public void setCssPat(String cssPat) {
		this.cssPat = cssPat;
	}

}
