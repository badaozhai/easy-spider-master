/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * @author xym
 * @date 2015年8月31日
 *
 */
public class MyStringUtil {

	private static Logger log = Logger.getLogger(MyStringUtil.class);

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
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> json2Map(String json) {
		Gson gson = new Gson();
		Map<String, Object> map = null;
		try {
			map = gson.fromJson(json, Map.class);
		} catch (Exception e) {
			log.info("json----------------------->" + json);
			log.error("LoginUtil.json2Map()-------------->" + e.getMessage());
		}
		return map;
	}
	
	/**
	 * 处理URL
	 * 
	 * @param refer
	 * @param url
	 * @return
	 */
	public static String tidyUrl(String refer, String url) {
		String nurl = "";
		try {
			if (!url.startsWith("http")) {
				URL u = new URL(refer);
				nurl = new URL(u, url).toString();
			} else {
				return url;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return nurl;
	}
}
