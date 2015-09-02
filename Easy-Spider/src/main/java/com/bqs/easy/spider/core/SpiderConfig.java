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
public class SpiderConfig{

	private Task t = null;

	public SpiderConfig(Task t) {
		TaskManager.FIRST_FIFO.add(t);
		this.t = t;
	}

	public Task getTask() {
		return t;
	}
}
