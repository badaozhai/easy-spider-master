/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.imp;

/**
 * 登陆接口，登陆的方法实现该方法<br>
 * 
 * @author xym
 * @date 2015年8月4日
 */
public interface ILogin {

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public IDownloader Login(String name, String password);

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @param util
	 * @return
	 */
	public boolean Login(IDownloader util, String name, String password);
}
