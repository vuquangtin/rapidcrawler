package com.codemineral.scheduler;

public interface Filter {
	public void add(String url);

	public boolean contains(String url);
}
