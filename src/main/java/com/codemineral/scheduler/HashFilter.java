package com.codemineral.scheduler;

import java.util.HashSet;

public class HashFilter implements Filter {
	private HashSet<String> set = new HashSet<String>();

	public synchronized void add(String url) {
		set.add(url);
	}

	public synchronized boolean contains(String url) {
		return set.contains(url);
	}
}
