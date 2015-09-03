/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.core;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.manager.TaskManager;

/**
 * @author xym
 * @date 2015年9月2日
 *
 */
public class Spider extends Thread {
	private SpiderConfig config = null;

	public Spider(SpiderConfig config) {
		this.config = config;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+config.getTask().getMainURL());
		try {
			Thread.sleep(23000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		runNextTask();
	}

	public void runNextTask() {
		TaskManager.FIRST_FIFO.remove(config.getTask());
		if (TaskManager.SECOND_FIFO.size() > 0) {
			Task nt = TaskManager.SECOND_FIFO.remove(0);
			SpiderConfig spiderConfig = new SpiderConfig(nt);
			new Spider(spiderConfig).start();
		}
	}
}
