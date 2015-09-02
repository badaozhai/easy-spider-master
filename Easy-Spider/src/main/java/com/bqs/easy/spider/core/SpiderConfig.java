/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.core;

import com.bqs.easy.spider.entity.Task;

/**
 * @author xym
 * @date 2015年9月2日
 *
 */
public class SpiderConfig implements Runnable {

	private Task t = null;

	public SpiderConfig(Task t) {
		TaskManager.FIRST_FIFO.add(t);
		this.t = t;
	}

	@Override
	public void run() {
		System.out.println(t.getMainURL());
		try {
			Thread.sleep(23000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		runNextTask();
	}

	public void runNextTask()  {
		TaskManager.FIRST_FIFO.remove(t);
		if (TaskManager.SECOND_FIFO.size() > 0) {
			Task nt = TaskManager.SECOND_FIFO.remove(0);
			SpiderConfig spiderConfig = new SpiderConfig(nt);
			new Thread(spiderConfig).start();
		}
	}

}
