package com.codemineral.core;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codemineral.analyzer.Analyzer;
import com.codemineral.downloader.Downloader;
import com.codemineral.downloader.HttpDownloader;
import com.codemineral.http.Page;
import com.codemineral.http.Request;
import com.codemineral.scheduler.QueueScheduler;
import com.codemineral.scheduler.Scheduler;
import com.codemineral.store.FileStore;
import com.codemineral.store.ResultBundle;
import com.codemineral.store.Store;

public class Crawler {
	private static Logger logger = LoggerFactory.getLogger(Crawler.class);

	private static final int STATE_INIT = 0;
	private static final int STATE_START = 1;
	private static final int STATE_COMPLETE = 2;

	private AtomicLong aliveTaskCount = new AtomicLong(0);
	private Store store;
	private Downloader downloader;
	private Analyzer analyzer;
	private Site site;
	private Scheduler scheduler;
	private volatile int state = STATE_INIT;
	private ExecutorService executorService;
	private ReentrantLock lock = new ReentrantLock();
	private Condition newUrlsCondition = lock.newCondition();
	private long waiteTimeBeforeExit = 5000;

	private Crawler() {
	}

	public static Crawler create() {
		return new Crawler();
	}

	private void initComponents() {
		if (site == null) {
			throw new NullPointerException("site cann't  be set null ! ");
		}
		if (store == null) {
			store = new FileStore();
		}
		if (downloader == null) {
			downloader = new HttpDownloader(1, site);
		}
		if (analyzer == null) {
			throw new NullPointerException("没有设置 analyzer！");
		}
		if (scheduler == null) {
			scheduler = new QueueScheduler();
		}
		List<Request> startRequests = site.getStartRequests();
		for (Request request : startRequests) {
			scheduler.push(request);
		}
		executorService = Executors.newFixedThreadPool(4);

	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public void setDownloader(Downloader downloader) {
		this.downloader = downloader;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void start() {
		logger.info("webcrawler start!");
		initComponents();
		state = STATE_START;
		while (state != STATE_COMPLETE) {
			Request request = scheduler.pull();
			if (request == null) {
				if (aliveTaskCount.get() == 0) {
					/*
					 * 没有存活的任务，也就表面没有可能获取新的url
					 */
					state = STATE_COMPLETE;
				} else {
					/*
					 * scheduler有可能从存活的任务中获取新的url
					 */
					try {
						lock.lock();
						while (aliveTaskCount.get() == 0) {
							try {
								newUrlsCondition.await();
							} catch (InterruptedException e) {
							}
						}
					} catch (Exception e) {

					} finally {
						lock.unlock();
					}
				}
			} else {
				aliveTaskCount.getAndIncrement();
				processRequest(request);
				try {
					Thread.sleep(site.getSleepTime());
				} catch (Exception e) {
				}
			}
		}
		logger.info("crawler exit.");
	}

	private void processRequest(final Request request) {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				try {
					Page page = downloader.download(request, site);
					page.setRequest(request);
					ResultBundle bundle = analyzer.process(page);
					store.save(bundle);
				} catch (Exception e) {
					logger.info("task error", e);
				}
				aliveTaskCount.getAndDecrement();
				/*
				 * 依然有可能并没有往scheduler中添加新的url，所以唤醒那里需要重新检查
				 */
				try {
					lock.lock();
					newUrlsCondition.notifyAll();
				} finally {
					lock.unlock();
				}
			}
		};
		executorService.submit(task);
	}

}
