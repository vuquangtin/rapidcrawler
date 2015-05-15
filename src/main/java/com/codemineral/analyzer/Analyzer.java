package com.codemineral.analyzer;

import com.codemineral.http.Page;
import com.codemineral.store.ResultBundle;

public interface Analyzer {
	ResultBundle process(Page page);
}
