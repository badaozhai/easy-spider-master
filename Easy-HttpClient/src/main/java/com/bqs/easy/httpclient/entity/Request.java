package com.bqs.easy.httpclient.entity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

/**
 * 
 * @author xym
 * @date 2015年9月3日
 */
public class Request implements Serializable {

	private static final long serialVersionUID = 1l;

	private String url;

	private String title;

	private int depth = 1;

	private String method = "get";

	private String referer = "";

	private Map<String, String> header = new LinkedHashMap<String, String>();

	/**
	 * 这里使用treemap，主要是为了在排重的时候使用
	 */
	private Map<String, String> postdata = new TreeMap<String, String>();
	private Map<String, String> extra = new TreeMap<String, String>();
	private Map<String, Object> extra1 = new TreeMap<String, Object>();

	public Request() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getMethod() {
		return method.toUpperCase();
	}

	public void setMethod(String method) {
		if (method != null && !"".equals(method)) {
			this.method = method;
		}
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Map<String, String> getHeader() {
		if (header == null) {
			header = new LinkedHashMap<String, String>();
		}
		if (!header.containsKey("referer") && null != referer && !"".equals(referer)) {
			header.put("referer", referer);
		}
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public void putHeader(String name, String value) {
		if (header == null) {
			header = new LinkedHashMap<String, String>();
		}
		header.put(name, value);
	}

	public void removeHeader(String name) {
		if (header == null) {
			header = new LinkedHashMap<String, String>();
		}
		header.remove(name);
	}

	public void putPostdata(String name, String value) {
		if (postdata == null) {
			postdata = new LinkedHashMap<String, String>();
		}
		postdata.put(name, value);
	}

	public Map<String, String> getPostdata() {
		return postdata;
	}

	public void setPostdata(Map<String, String> postdata) {
		this.postdata = postdata;
	}

	public void removePostdata(String name) {
		if (postdata == null) {
			postdata = new LinkedHashMap<String, String>();
		}
		postdata.remove(name);
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}

	public void putExtra(String name, String value) {
		if (extra == null) {
			extra = new LinkedHashMap<String, String>();
		}
		extra.put(name, value);
	}

	public void removeExtra(String name) {
		if (extra == null) {
			extra = new LinkedHashMap<String, String>();
		}
		extra.remove(name);
	}

	public void putExtra1(String name, Object value) {
		if (extra1 == null) {
			extra1 = new LinkedHashMap<String, Object>();
		}
		extra1.put(name, value);
	}

	public void removeExtra1(String name) {
		if (extra1 == null) {
			extra1 = new LinkedHashMap<String, Object>();
		}
		extra1.remove(name);
	}

	public Map<String, Object> getExtra1() {
		return extra1;
	}

	public void setExtra1(Map<String, Object> extra1) {
		this.extra1 = extra1;
	}

	public Request(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Request(int depth, String url) {
		this.depth = depth;
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((postdata == null) ? 0 : postdata.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Request other = (Request) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (postdata == null) {
			if (other.postdata != null)
				return false;
		} else if (!postdata.equals(other.postdata))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
