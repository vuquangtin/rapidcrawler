package com.codemineral.extractor;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexExtractor implements Extractor {
	private static Logger logger = LoggerFactory
			.getLogger(RegexExtractor.class);

	private Pattern pattern;

	private RegexExtractor(Pattern pattern) {
		this.pattern = pattern;
	}

	public static Extractor create(String regexPat) {
		try {
			Pattern pattern = Pattern.compile(regexPat);
			return new RegexExtractor(pattern);
		} catch (PatternSyntaxException e) {
			logger.warn("regex extractor parsing error", e);
			throw e;
		}
	}

	@Override
	public String extract(String raw) {
		List<String> list = extractAll(raw);
		if (list.isEmpty()) {
			return StringUtils.EMPTY;
		}
		return list.get(0);
	}

	@Override
	public List<String> extractAll(String raw) {
		List<String> list = new LinkedList<String>();
		Matcher matcher = pattern.matcher(raw);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}

	@Override
	public List<String> extracAll(List<String> rawList) {
		List<String> list = new LinkedList<String>();
		for (String raw : rawList) {
			String result = extract(raw);
			if (result.equals(StringUtils.EMPTY)) {
				list.add(result);
			}
		}
		return list;
	}

}
