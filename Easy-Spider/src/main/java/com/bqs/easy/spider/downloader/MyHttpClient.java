package com.bqs.easy.spider.downloader;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.log4j.Logger;

import com.bqs.easy.httpclient.core.EasyHttpClient;
import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.imp.IDownloader;

public class MyHttpClient extends EasyHttpClient implements IDownloader {

	private static Logger log = Logger.getLogger(MyHttpClient.class);

	public String requestText(Request request) {

		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] text start . ");
		HttpUriRequest requesturi = requestHelp(request);
		String text = execute_text("", request.getHeader(), requesturi);
		log.info(request.getMethod() + " : [ " + request.getUrl() + " ]  text end . ");

		return text;
	}

	public byte[] requestBytes(Request request) {

		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] bytes start . ");
		HttpUriRequest requesturi = requestHelp(request);
		byte[] bytes = execute_byte(requesturi, request.getHeader());
		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] bytes end . ");

		return bytes;
	}

	private HttpUriRequest requestHelp(Request request) {
		HttpUriRequest requesturi = getHttpUriRequest(request.getUrl(), request.getMethod(), request.getPostdata(), request.getHeader());
		return requesturi;
	}
}
