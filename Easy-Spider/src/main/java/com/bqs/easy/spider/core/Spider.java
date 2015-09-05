/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.core;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.bqs.easy.httpclient.entity.Page;
import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.manager.TaskManager;

/**
 * @author xym
 * @date 2015年9月2日
 *
 */
public class Spider extends Thread {

	private static Logger log = Logger.getLogger(Spider.class);

	private SpiderConfig config = null;

	private CountDownLatch latch = null;

	public Spider(SpiderConfig config, CountDownLatch latch) {
		this.config = config;
		this.latch = latch;
	}

	@Override
	public void run() {
		Request r = config.getQueues().poll();
		while (r != null) {
			log.info("queue size : " + config.getQueues().size() + " , depth : " + r.getDepth() + " ,url [ "
					+ r.getUrl() + " ] start .");
			Page p = config.getDownloader().requestText(r);
			System.out.println(p.getHtml().length());
			log.info("url [ " + r.getUrl() + " ] end .");
			int maxdepth = config.getTask().getDepth();
			int depth = r.getDepth();
			if (depth < maxdepth) {
				List<Request> list = config.getExtractionhrefs().parserLinksInHTML(r.getUrl(), p.getHtml(), null,
						p.getCharset());
				config.addRequests(list, depth);
			}
			config.getVisitedURL().add(r.getUrl());
			r = config.getQueues().poll();
			log.info("=========================================");
		}

		latch.countDown();

		if (latch.getCount() == 0L) {
			config.getDownloader().close();
			log.info("task [ " + config.getTask() + " ] end .");
			runNextTask();
		}

	}

	/**
	 * 如果等待队列中存在任务，运行等待队列中的任务
	 */
	public void runNextTask() {
		TaskManager.FIRST_FIFO.remove(config.getTask());
		if (TaskManager.SECOND_FIFO.size() > 0) {
			Task nt = TaskManager.SECOND_FIFO.remove(0);
			SpiderConfig spiderConfig = new SpiderConfig(nt);
			CountDownLatch latch = new CountDownLatch(nt.getThreadNum());
			for (int i = 0; i < nt.getThreadNum(); i++) {
				new Spider(spiderConfig, latch).start();
			}
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		}
	}
}
