/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.HttpClient.MyHttpClient;
import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.manager.TaskManager;

/**
 * @author xym
 * @date 2015年9月2日
 *
 */
public class SpiderConfig {

	private Task t = null;
	private MyHttpClient httpclient = null;

	BlockingQueue<Request> queues = new LinkedBlockingQueue<Request>();

	public SpiderConfig(Task t) {
		TaskManager.FIRST_FIFO.add(t);
		this.t = t;
		httpclient = new MyHttpClient();
	}

	public Task getTask() {
		return t;
	}

	public MyHttpClient getHttpclient() {
		return httpclient;
	}
}
