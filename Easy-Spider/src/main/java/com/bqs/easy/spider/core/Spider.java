/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.core;

import org.apache.log4j.Logger;

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

	public Spider(SpiderConfig config) {
		this.config = config;
	}

	@Override
	public void run() {
		config.firstPage();
		Request r = config.getQueues().poll();
		while (r != null) {
			log.info("queue size : " + config.getQueues().size() + " ,url [ " + r.getUrl() + " ] start .");
			String html = config.getHttpclient().requestText(r);
			System.out.println(html.length());
			log.info("url [ " + r.getUrl() + " ] end .");
			r = config.getQueues().poll();
			log.info("=========================================");
		}

		log.info("task [ " + config.getTask() + " ] end .");

		runNextTask();
	}

	/**
	 * 如果等待队列中存在任务，运行等待队列中的任务
	 */
	public void runNextTask() {
		TaskManager.FIRST_FIFO.remove(config.getTask());
		if (TaskManager.SECOND_FIFO.size() > 0) {
			Task nt = TaskManager.SECOND_FIFO.remove(0);
			SpiderConfig spiderConfig = new SpiderConfig(nt);
			new Spider(spiderConfig).start();
		}
	}
}
