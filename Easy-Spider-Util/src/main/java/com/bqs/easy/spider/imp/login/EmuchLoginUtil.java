/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.imp.login;

import org.apache.log4j.Logger;

import com.bqs.easy.httpclient.core.EasyHttpClient;
import com.bqs.easy.spider.imp.IDownloader;
import com.bqs.easy.spider.imp.ILogin;
import com.bqs.easy.spider.impl.downloader.HttpClientDownloader;
import com.bqs.easy.util.LoginUtilInterface;
import com.bqs.loginutil.test._365EDaiLoginUtil;

/**
 * @author xym
 * @date 2015年9月16日
 *
 */
public class EmuchLoginUtil implements ILogin {

	private static Logger log = Logger.getLogger(EmuchLoginUtil.class);

	@Override
	public IDownloader Login(String name, String password) {
		IDownloader util = new HttpClientDownloader();
		boolean status = Login(util, name, password);
		if (status) {
			return util;
		} else {
			return null;
		}
	}

	@Override
	public boolean Login(IDownloader util, String name, String password) {
		return false;
	}

	/**
	 * 小霸王1234 小霸王1234
	 */
	public static void main(String[] args) {
		// 自己的账号，口令
		String name = "";
		String password = "";

		ILogin Loginutil = new EmuchLoginUtil();
		IDownloader util = new HttpClientDownloader();
		Loginutil.Login(util, name, password);
		util.close();
	}
}
