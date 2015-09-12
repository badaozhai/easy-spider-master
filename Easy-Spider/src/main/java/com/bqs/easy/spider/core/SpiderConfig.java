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

import com.bqs.easy.httpclient.entity.Page;
import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.downloader.HttpClientDownloader;
import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.imp.BloomFilterDuplicateRemover;
import com.bqs.easy.spider.imp.IDownloader;
import com.bqs.easy.spider.imp.IDuplicateRemover;
import com.bqs.easy.spider.imp.IExtractionHrefAble;
import com.bqs.easy.spider.imp.ILogin;
import com.bqs.easy.spider.imp.IPipeline;
import com.bqs.easy.spider.manager.TaskManager;
import com.bqs.easy.spider.parser.ExtractionHref;
import com.bqs.easy.spider.pipeline.ConsolePipeline;

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
	private IDownloader downloader = null;

	/**
	 * 连接提取模块
	 */
	private IExtractionHrefAble extractionhrefs = null;

	/**
	 * 已处理队列
	 */
	private Set<String> visitedURL = new LinkedHashSet<String>();

	private IDuplicateRemover remover = null;

	private ILogin login = null;

	private IPipeline pipeLine = null;
	/**
	 * 待处理队列
	 */
	// private Set<String> visitingURL = new LinkedHashSet<String>();
	/**
	 * 任务队列
	 */
	BlockingQueue<Request> queues = new LinkedBlockingQueue<Request>();

	public SpiderConfig(Task t) {
		log.info("task [ " + t + " ] end .");
		TaskManager.FIRST_FIFO.add(t);
		this.t = t;
		downloader = new HttpClientDownloader();
		extractionhrefs = new ExtractionHref();
		pipeLine = new ConsolePipeline();
		remover = new BloomFilterDuplicateRemover(t);
		if (t.isIslogin()) {
			String username = t.getUsername();
			String password = t.getPassword();
			if (null == username || "".equals(username)) {
				log.error("用户名为空");
			}
			if (null == password || "".equals(password)) {
				log.error("密码为空");
			}
			boolean loginstatus = login.Login(downloader, username, password);
			if (loginstatus) {
				firstPage();
			} else {
				return;
			}
		} else {
			firstPage();
		}

	}

	/**
	 * 采集任务的第一页，入口页
	 */
	public void firstPage() {
		Page p = downloader.requestText(t.getRequest());
		List<Request> list = extractionhrefs.parserLinksInHTML(t.getMainURL(), p.getHtml(), null, p.getCharset());
		addRequests(list, 0);
	}

	/**
	 * 将url放到队列当中
	 * 
	 * @param requests
	 * @param depth
	 */
	public synchronized void addRequests(List<Request> requests, int depth) {
		int cdepth = depth + 1;
		for (Request request : requests) {
			String url = request.getUrl();
			if (!remover.isContainStr(url) && !queues.contains(request)) {
				request.setDepth(cdepth);
				log.info("put , Method : [ " + request.getMethod() + " ] Depth : " + cdepth + " , Url - "
						+ request.getUrl() + " | " + request.getTitle());
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

	public SpiderConfig setDownloader(IDownloader downloader) {
		if (downloader != null) {
			this.downloader = downloader;
		} else {
			this.downloader = new HttpClientDownloader();
		}
		return this;
	}

	public SpiderConfig setPipeLine(IPipeline pipeLine) {
		if (pipeLine != null) {
			this.pipeLine = pipeLine;
		} else {
			this.pipeLine = new ConsolePipeline();
		}
		return this;
	}

	public SpiderConfig setLogin(ILogin login) {
		this.login = login;
		return this;
	}

	public SpiderConfig setRemover(IDuplicateRemover remover) {
		this.remover = remover;
		return this;
	}

	public IPipeline getPipeLine() {
		return pipeLine;
	}

	public IDownloader getDownloader() {
		return downloader;
	}

	public BlockingQueue<Request> getQueues() {
		return queues;
	}

	public Set<String> getVisitedURL() {
		return visitedURL;
	}
	
	public IDuplicateRemover getRemover() {
		return remover;
	}
}
