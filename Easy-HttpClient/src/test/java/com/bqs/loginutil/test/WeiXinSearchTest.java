/**
 * Copyright (C) 2015 xym, Inc. All Rights Reserved.
 */
package com.bqs.loginutil.test;

import com.bqs.easy.httpclient.core.EasyHttpClient;
import com.bqs.easy.util.JsUtil;

/**
 * @author xym
 * @date 2015年8月27日
 *
 */
public class WeiXinSearchTest {

	public static void main(String[] args) {
		EasyHttpClient httpclient = new EasyHttpClient();
		String s = httpclient.Get("http://weixin.sogou.com/gzh?openid=oIWsFtx3aZliGrMiDSj-46MO-fbo");
		System.out.println(httpclient.getCharset());
		System.out.println(httpclient.getLastmodify());
		System.out.println(httpclient.getCookieStore());
		System.out.println(httpclient.getStatusCode());
		System.out.println(httpclient.getContent_type());
		System.out.println(s);
		s = httpclient
				.Get("http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=oIWsFtx3aZliGrMiDSj-46MO-fbo&eqs=UasyopzgEv16oQlDcrCFPukotv1PXyW3fy0XutHiZcYmvQXIoCncUWaQ1XDGIS1SxX82B&ekv=3&page=1&t=1441869057220");
		System.out.println(httpclient.getCharset());
		System.out.println(httpclient.getLastmodify());
		System.out.println(httpclient.getCookieStore());
		System.out.println(httpclient.getStatusCode());
		System.out.println(httpclient.getContent_type());
		System.out.println(s);

		httpclient.close();

	}
}
