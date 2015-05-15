package com.codemineral.store;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileStore implements Store {
	private static Logger logger = LoggerFactory.getLogger(FileStore.class);

	private File dir;

	public FileStore() {
		this("/webcrawler/");
	}

	public FileStore(String path) {
		dir = new File(path);
	}

	@Override
	public void save(ResultBundle bundle) {
		String url = bundle.getRequest().getUrl();
		String hash = DigestUtils.md5Hex(url);
		File file = new File(dir, hash + ".txt");
		try {
			OutputStream out = FileUtils.openOutputStream(file);
			Writer writer = new OutputStreamWriter(out, "utf-8");
			Iterator<Object> oIterator = bundle.keyIterator();
			while (oIterator.hasNext()) {
				Object key = oIterator.next();
				Object val = bundle.get(key);
				writer.write(key.toString());
				writer.write(": ");
				writer.write(val.toString());
				writer.write("\r\n");
			}
			writer.close();
		} catch (IOException e) {
			logger.warn("存储数据异常！", e);
		}
	}
}
