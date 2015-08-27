/**
 * Copyright (C) 2015 xym, Inc. All Rights Reserved.
 */
package com.bqs.loginutil.test;

import com.bqs.easy.core.EasyHttpClient;
import com.bqs.easy.util.JsUtil;

/**
 * @author xym
 * @date 2015年8月27日
 *
 */
public class EasyHttpClientTest {

	public static void main(String[] args) {
		EasyHttpClient httpclient = new EasyHttpClient();
		String s = httpclient.Get("http://ruokuai.com/");
		System.out.println(httpclient.getCharset());
		System.out.println(httpclient.getLastmodify());
		System.out.println(httpclient.getCookieStore());
		System.out.println(httpclient.getStatusCode());
		System.out.println(httpclient.getContent_type());
		System.out.println(s.length());

		byte[] b = httpclient.GetImg("http://ruokuai.com/Resources/images/banner1.jpg");
		System.out.println(b.length);

		httpclient.close();

		String jquerymd5 = JsUtil.readJs("/spider/my089.md5.js");

		String a = "";
		try {
			a = JsUtil.runJs(jquerymd5, "MD5", new Object[] { "aaa", "32" });
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(a);
	}
}
