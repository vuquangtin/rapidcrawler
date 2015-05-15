package com.codemineral.extractor;

import java.util.List;

public interface Extractor {
	public String extract(String raw);

	public List<String> extractAll(String raw);

	public List<String> extracAll(List<String> rawList);
}
