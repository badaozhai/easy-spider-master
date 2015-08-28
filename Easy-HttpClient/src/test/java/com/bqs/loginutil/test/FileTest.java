/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.loginutil.test;

import org.apache.commons.lang3.StringEscapeUtils;

import com.bqs.easy.core.FileEasyHttpClient;

/**
 * @author xym
 * @date 2015年8月27日
 *
 */
public class FileTest {

	public static void main(String[] args) {
		FileEasyHttpClient httpclient = new FileEasyHttpClient();
		httpclient.Get("http://shitu.baidu.com/");
		String url = "http://localhost:8080/SpringMvCTest/upload.do";
		String hh = httpclient.upload_Post(url, null, "file", "C:\\Users\\wetime\\Desktop\\bjs.sql");
		System.out.println(StringEscapeUtils.unescapeJson(hh));
	}
}
