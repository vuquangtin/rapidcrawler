package com.codemineral.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codemineral.http.Request;

public class QueueScheduler implements Scheduler {
	private static Logger logger = LoggerFactory
			.getLogger(QueueScheduler.class);

	private HashFilter duplicater = new HashFilter();
	private BlockingQueue<String> taskQueue = new LinkedBlockingQueue<String>();

	@Override
	public Request pull() {
		String target = taskQueue.poll();
		if (target == null) {
			return null;
		}
		duplicater.add(target);
		Request request = new Request(target);
		return request;
	}

	@Override
	public void push(Request request) {

		try {
			if (!duplicater.contains(request.getUrl())) {
				taskQueue.put(request.getUrl());
			}
		} catch (InterruptedException e) {
			logger.info("push interrupt", e);
		}

	}

}
