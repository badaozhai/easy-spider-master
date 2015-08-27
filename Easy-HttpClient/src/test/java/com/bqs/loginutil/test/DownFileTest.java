/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.loginutil.test;

import com.bqs.easy.core.FileEasyHttpClient;

/**
 * @author xym
 * @date 2015年8月27日
 *
 */
public class DownFileTest {

	public static void main(String[] args) {
		FileEasyHttpClient httpclient = new FileEasyHttpClient();
		httpclient.down_Get("http://blog.csdn.net/lianghongge/article/details/42120751", null, "logs/a.txt");
		httpclient.down_Post("http://blog.csdn.net/lianghongge/article/details/42120751", null, null, "logs/a.txt");
	}
}
