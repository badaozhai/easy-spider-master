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
		String url="http://shitu.baidu.com/n/image?fr=html5&target=pcSearchImage&needJson=true&id=WU_FILE_0&name=X%60EWGTIAX(KHIKJ0O99G9TJ.jpg&type=image%2Fjpeg&lastModifiedDate=Wed+Aug+26+16%3A47%3A25+UTC%2B0800+2015&size=75960";
		String hh=httpclient.upload_Post(url, null, "C:\\Users\\wetime\\Desktop\\1.jpg");
		System.out.println(StringEscapeUtils.unescapeJson(hh));
	}
}
