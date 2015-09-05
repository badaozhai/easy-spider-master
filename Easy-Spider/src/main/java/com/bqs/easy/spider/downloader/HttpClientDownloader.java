package com.bqs.easy.spider.downloader;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.log4j.Logger;

import com.bqs.easy.httpclient.core.EasyHttpClient;
import com.bqs.easy.httpclient.entity.Page;
import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.imp.IDownloader;

public class HttpClientDownloader extends EasyHttpClient implements IDownloader {

	private static Logger log = Logger.getLogger(HttpClientDownloader.class);

	public Page requestText(Request request) {

		String text = "";
		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] text start . ");
		try {
			HttpUriRequest requesturi = requestHelp(request);
			text = execute_text("", request.getHeader(), requesturi);
			log.info(request.getMethod() + " : [ " + request.getUrl() + " ]  text end . ");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		Page p = new Page();
		p.setHtml(text);
		p.setCharset(getCharset());
		p.setContent_type(getContent_type());
		p.setLastmodify(getLastmodify());
		p.setStatusCode(getStatusCode());

		return p;
	}

	public Page requestBytes(Request request) {

		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] bytes start . ");
		HttpUriRequest requesturi = requestHelp(request);
		byte[] bytes = execute_byte(requesturi, request.getHeader());
		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] bytes end . ");
		Page p = new Page();
		p.setCharset(getCharset());
		p.setContent_type(getContent_type());
		p.setLastmodify(getLastmodify());
		p.setStatusCode(getStatusCode());
		p.setBytes(bytes);
		return p;
	}

	private HttpUriRequest requestHelp(Request request) {
		HttpUriRequest requesturi = getHttpUriRequest(request.getUrl(), request.getMethod(), request.getPostdata(),
				request.getHeader());
		return requesturi;
	}
}
