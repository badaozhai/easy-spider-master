/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.util;

/**
 * @author xym
 * @date 2015年8月31日
 *
 */
public class MyStringUtil {

	public static boolean readBoolean(Object o) {
		if (o == null) {
			return false;
		}
		String str = "" + o;
		str = str.toLowerCase();
		if ("".equals(str) || "0".equals(str) || "false".equals(str) || "0".equals(str)) {
			return false;
		} else {
			return true;
		}
	}
}
