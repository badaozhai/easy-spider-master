/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.core;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.downloader.MyHttpClient;
import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.imp.IExtractionHrefAble;
import com.bqs.easy.spider.manager.TaskManager;
import com.bqs.easy.spider.parser.ExtractionHref;

/**
 * 采集配置
 * 
 * @author xym
 * @date 2015年9月2日
 *
 */
public class SpiderConfig {
	private static Logger log = Logger.getLogger(SpiderConfig.class);

	/**
	 * 将要采集的任务
	 */
	private Task t = null;
	/**
	 * 访问网页的方法
	 */
	private MyHttpClient httpclient = null;

	/**
	 * 连接提取模块
	 */
	private IExtractionHrefAble extractionhrefs = null;

	/**
	 * 已处理队列
	 */
	private Set<String> visitedURL = new LinkedHashSet<String>();
	/**
	 * 待处理队列
	 */
	private Set<String> visitingURL = new LinkedHashSet<String>();
	/**
	 * 任务队列
	 */
	BlockingQueue<Request> queues = new LinkedBlockingQueue<Request>();

	public SpiderConfig(Task t) {
		log.info("task [ " + t + " ] end .");
		TaskManager.FIRST_FIFO.add(t);
		this.t = t;
		httpclient = new MyHttpClient();
		extractionhrefs = new ExtractionHref();

	}

	/**
	 * 采集任务的第一页，入口页
	 */
	public void firstPage() {
		String html = httpclient.requestText(t.getRequest());
		List<Request> list = extractionhrefs.parserLinksInHTML(t.getMainURL(), html, null, httpclient.getCharset());
		for (Request request : list) {
			if (!queues.contains(request)) {
				log.info("put , Method : [ "+request.getMethod()+" ] , Url - "+request.getUrl()+" | "+request.getTitle());
				queues.add(request);
			}
		}
	}

	public Task getTask() {
		return t;
	}

	public IExtractionHrefAble getExtractionhrefs() {
		return extractionhrefs;
	}

	/**
	 * 设置连接提取模块
	 * 
	 * @param extractionhrefs
	 * @return
	 */
	public SpiderConfig setExtractionhrefs(IExtractionHrefAble extractionhrefs) {
		if (extractionhrefs != null) {
			this.extractionhrefs = extractionhrefs;
		} else {
			this.extractionhrefs = new ExtractionHref();
		}
		return this;
	}

	public MyHttpClient getHttpclient() {
		return httpclient;
	}

	public BlockingQueue<Request> getQueues() {
		return queues;
	}
}
