package com.bqs.easy.httpclient.entity;

public class Page {

	private Request request;

	private String html;

	private int statusCode;

	private String content_type;
	private String charset;
	private long lastmodify;
	private byte[] bytes;

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public long getLastmodify() {
		return lastmodify;
	}

	public void setLastmodify(long lastmodify) {
		this.lastmodify = lastmodify;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
