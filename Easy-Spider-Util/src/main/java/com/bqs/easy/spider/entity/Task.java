﻿package com.bqs.easy.spider.entity;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.util.Variable;
import com.google.gson.Gson;

/**
 * 任务
 * 
 * @author xym
 * @date 2015年8月29日
 */
public class Task implements Serializable {

	private static final long serialVersionUID = 1l;

	/** 站点名称,元搜索时关键词 */
	private String websiteName = ""; // 元搜索时为 关键词

	/** 入口地址 */
	private String mainURL = "";

	/** 暂停 多少毫秒之后继续访问下一个 URL */
	private int pauseTime = 0;

	/** 是否删除 */
	private boolean isDeleted = false;

	/** 采集深度 */
	private int depth = 1;

	/** 是否采用代理 */
	private int isProxy = 0;

	/** 代理 主机 */
	private String proxyHost = "127.0.0.1";

	/** 代理 端口 */
	private int proxyPort = 8080;

	/** 任务状态 */
	private int status = 0;

	/** 线程数 */
	private int threadNum = 1;

	/** 定时器参数,默认为 每隔一小时更新一次 */
	private String quartzParam = "0/10  0/1  *  *  *  ?";

	/** 本次采集是否需要登陆 */
	private boolean islogin = false;
	/** 用户名 */
	private String username = "";
	/** 密码 */
	private String password = "";

	private Request request = null;

	private String plug_Downloader = "com.bqs.easy.spider.impl.downloader.HttpClientDownloader";
	private String plug_Extractionhrefs = "com.bqs.easy.spider.impl.hrefable.ExtractionHref";
	private String plug_PipeLine = "com.bqs.easy.spider.impl.pipeline.ConsolePipeline";
	private String plug_Remover = "com.bqs.easy.spider.impl.remover.BloomFilterDuplicateRemover";
	private String plug_Login = "";

	private AtomicInteger counter;

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getMainURL() {
		return mainURL;
	}

	public void setMainURL(String mainURL) {
		this.mainURL = mainURL;
	}

	public int getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(int pauseTime) {
		this.pauseTime = pauseTime;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getIsProxy() {
		return isProxy;
	}

	public void setIsProxy(int isProxy) {
		this.isProxy = isProxy;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getQuartzParam() {
		return quartzParam;
	}

	public void setQuartzParam(String quartzParam) {
		this.quartzParam = quartzParam;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Request getRequest() {
		if (request == null) {
			request = new Request(mainURL);
		}
		return request;
	}

	public int getThreadNum() {
		if (threadNum < 1) {
			return 1;
		}
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public boolean isIslogin() {
		return islogin;
	}

	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}

	public AtomicInteger getCounter() {
		if (counter == null) {
			counter = new AtomicInteger(0);
		}
		return counter;
	}

	public void setCounter(AtomicInteger counter) {
		this.counter = counter;
	}

	public String getPlug_Downloader() {
		if (plug_Downloader == null) {
			plug_Downloader = Variable.httpclientdownloader;
		}
		return plug_Downloader;
	}

	public void setPlug_Downloader(String plug_Downloader) {
		this.plug_Downloader = plug_Downloader;
	}

	public String getPlug_Extractionhrefs() {
		if (plug_Extractionhrefs == null) {
			plug_Extractionhrefs = Variable.extractionhrefs;
		}
		return plug_Extractionhrefs;
	}

	public void setPlug_Extractionhrefs(String plug_Extractionhrefs) {
		this.plug_Extractionhrefs = plug_Extractionhrefs;
	}

	public String getPlug_PipeLine() {
		if (plug_PipeLine == null) {
			plug_PipeLine = Variable.pipeLine;
		}
		return plug_PipeLine;
	}

	public void setPlug_PipeLine(String plug_PipeLine) {
		this.plug_PipeLine = plug_PipeLine;
	}

	public String getPlug_Remover() {
		if (plug_Remover == null) {
			plug_Remover = Variable.remover;
		}
		return plug_Remover;
	}

	public void setPlug_Remover(String plug_Remover) {
		this.plug_Remover = plug_Remover;
	}

	public String getPlug_Login() {
		if (plug_Login == null) {
			plug_Login = "";
		}
		return plug_Login;
	}

	public void setPlug_Login(String plug_Login) {
		this.plug_Login = plug_Login;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mainURL == null) ? 0 : mainURL.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (mainURL == null) {
			if (other.mainURL != null)
				return false;
		} else if (!mainURL.equals(other.mainURL))
			return false;
		return true;
	}

}
