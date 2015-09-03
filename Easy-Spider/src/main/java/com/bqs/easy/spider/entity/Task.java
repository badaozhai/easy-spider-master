package com.bqs.easy.spider.entity;

import java.io.Serializable;

import com.bqs.easy.httpclient.entity.Request;
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

	/** 定时器参数,默认为 每隔一小时更新一次 */
	private String quartzParam = "0/10  0/1  *  *  *  ?";

	private Request request = null;

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

	public Request getRequest() {
		if (request == null) {
			request = new Request(mainURL);
		}
		return request;
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
