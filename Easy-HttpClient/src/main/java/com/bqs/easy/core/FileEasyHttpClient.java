/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * 上传和下载文件，上传暂未完成
 * @author xym
 * @date 2015年8月27日
 *
 */
public class FileEasyHttpClient extends EasyHttpClient {

	private static Logger log = Logger.getLogger(EasyHttpClient.class);

	/**
	 * 通过post方法下载文件，未测试
	 * @param posturl 提交的URL
	 * @param header 请求头
	 * @param postdata 请求参数
	 * @param charset 字符集编码
	 * @return
	 * @throws Exception
	 */
	public boolean down_Post(String posturl, Map<String, String> header, Map<String, String> postdata, String filepath) {
		boolean status = false;
		if (null == filepath || "".equals(filepath)) {
			return status;
		}
		try {
			log.info("begin post down url : [ " + posturl + " ] .");
			HttpUriRequest request = getHttpUriRequest(posturl, "post", postdata, header);
			status = down_file(request, header, filepath);
			log.info("end post down url : [ " + posturl + " ] .");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return status;
	}

	/**
	 * @param posturl 提交的URL
	 * @param header 请求头
	 * @param postdata 请求参数
	 * @param charset 字符集编码
	 * @return
	 * @throws Exception
	 */
	public boolean down_Get(String url, Map<String, String> header, String filepath) {
		boolean status = false;
		if (null == filepath || "".equals(filepath)) {
			return status;
		}
		try {
			log.info("begin get down url : [ " + url + " ] .");
			HttpUriRequest request = getHttpUriRequest(url, "get", null, header);
			status = down_file(request, header, filepath);
			log.info("end get down url : [ " + url + " ] .");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return status;
	}

	/**
	 * 执行请求，返回字节
	 * 
	 * @param charset
	 * @param httpUriRequest
	 * @param filepath
	 * @return
	 */
	private boolean down_file(HttpUriRequest httpUriRequest, Map<String, String> header, String filepath) {
		boolean status = false;
		HttpEntity entity = null;
		try {
			String url = httpUriRequest.getURI().toURL().toExternalForm();
			CloseableHttpResponse httpResponse = getHttpClient().execute(httpUriRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			entity = httpResponse.getEntity();
			Header heade = entity.getContentType();
			if (heade != null) {
				log.info("statusCode : " + statusCode + " ContentType : " + heade.getValue());
				setContent_type(heade.getValue());
			} else {
				log.info("statusCode : " + statusCode + " ContentType : unknown .");
			}
			setStatusCode(statusCode);
			if (statusCode == 200) {
				status = down(url, entity, filepath);
			} else if (statusCode == 302 || statusCode == 300 || statusCode == 301) {
				URL referer = httpUriRequest.getURI().toURL();
				httpUriRequest.abort();
				Header location = httpResponse.getFirstHeader("Location");
				String locationurl = location.getValue();
				if (!locationurl.startsWith("http")) {
					URL u = new URL(referer, locationurl);
					locationurl = u.toExternalForm();
				}
				status = down_Get(locationurl, header, filepath);
			} else {
				status = down(url, entity, filepath);
			}

		} catch (ClientProtocolException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (httpUriRequest != null) {
				httpUriRequest.abort();
			}
			if (entity != null) {
				EntityUtils.consumeQuietly(entity);
			}
		}
		return status;
	}

	private boolean down(String url, HttpEntity entity, String filepath) {
		boolean status = false;
		File file = new File(filepath);
		log.info("begin write file to : [ " + file.toURI().getPath() + " ] . this url is [ " + url + " ]");
		InputStream in = null;
		FileOutputStream fout = null;
		try {
			in = entity.getContent();
			fout = new FileOutputStream(file);
			int l = -1;
			byte[] tmp = new byte[1024];
			while ((l = in.read(tmp)) != -1) {
				fout.write(tmp, 0, l);
			}
			fout.flush();
			status = true;
		} catch (IllegalStateException | IOException e) {
			log.error(e.getMessage());
		} finally {// 关闭流。
			try {
				if (fout != null) {
					fout.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		log.info("write file status : " + status + " , file is : [ " + file.toURI().getPath() + " ] end .");
		return status;
	}
}
