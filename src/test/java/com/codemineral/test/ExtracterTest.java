package com.codemineral.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.codemineral.extractor.CssExtractor;
import com.codemineral.extractor.Extractor;

public class ExtracterTest {

	@Test
	public void testCss() throws IOException {
		InputStream in = ClassLoader.getSystemResourceAsStream("answer.html");
		String html = IOUtils.toString(in, "utf-8");
		Extractor extractor = CssExtractor.build("head title");
		String title = extractor.extract(html);
		System.out.println(title);
	}

	@Test
	public void testXpath() {

	}
}
