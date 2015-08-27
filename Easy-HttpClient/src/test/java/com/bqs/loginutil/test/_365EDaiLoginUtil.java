/**
 * Copyright (C) 2015 xym, Inc. All Rights Reserved.
 */
package com.bqs.loginutil.test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bqs.easy.core.EasyHttpClient;
import com.bqs.easy.util.LoginUtilInterface;

/**
 * 365e贷登陆
 * @author xym
 * @date 2015年8月4日
 */
public class _365EDaiLoginUtil implements LoginUtilInterface {

	private static Logger log = Logger.getLogger(_365EDaiLoginUtil.class);

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public EasyHttpClient Login(String name, String password) {
		EasyHttpClient util = new EasyHttpClient();
		boolean status = Login(util, name, password);
		if (status) {
			return util;
		} else {
			return null;
		}
	}

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @param util
	 * @return
	 */
	public boolean Login(EasyHttpClient util, String name, String password) {

		String loginhtml = util.Get("https://www.365edai.cn/Index/login.aspx");

		Document doc = Jsoup.parse(loginhtml);
		String __EVENTARGUMENT = doc.select("#__EVENTARGUMENT").attr("value");
		String __VIEWSTATE = doc.select("#__VIEWSTATE").attr("value");
		String hdRequestReferer = doc.select("#hdRequestReferer").attr("value");
		Map<String, String> postdata = new LinkedHashMap<String, String>();
		Map<String, String> header = new LinkedHashMap<String, String>();
		header.put("Referer", "https://www.365edai.cn/Index/login.aspx");//
		header.put("Content-Type", "application/x-www-form-urlencoded");

		postdata.put("txt_nickname", name);
		postdata.put("txtPassword", password);
		postdata.put("__EVENTTARGET", "btnLogin");
		postdata.put("__EVENTARGUMENT", __EVENTARGUMENT);
		postdata.put("__VIEWSTATE", __VIEWSTATE);
		postdata.put("hdRequestReferer", hdRequestReferer);
		postdata.put("hfNickname", "");
		postdata.put("hfSevenLogin", "");

		String status = TryIt(util, header, postdata, 3);
		log.info("user " + name + " login status : " + status);
		if (null != status && "0".equals(status)) {
			return true;
		} else {
			return false;
		}

	}

	public static String TryIt(EasyHttpClient util, Map<String, String> header, Map<String, String> postdata, int trytimes) {
		String status = "";
		String s = util.Post("https://www.365edai.cn/Index/login.aspx", header, postdata);
		Document doc = Jsoup.parse(s);
		String user = doc.select("div.myAccount").text();
		if (!"".equals(user) && user.contains("欢迎您")) {
			return "0";
		}
		return status;
	}

	public static void main(String[] args) throws Exception {

		// 自己的账号，口令
		String name = "";
		String password = "";

		LoginUtilInterface Loginutil = new _365EDaiLoginUtil();
		EasyHttpClient util = new EasyHttpClient();
		Loginutil.Login(util, name, password);
		util.close();
	}

}
