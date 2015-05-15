package com.codemineral.scheduler;

import com.codemineral.http.Request;

public interface Scheduler {
	Request pull();

	void push(Request request);

}
