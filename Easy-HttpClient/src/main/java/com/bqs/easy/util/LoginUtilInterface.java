/**
 * Copyright (C) 2015 xym, Inc. All Rights Reserved.
 */
package com.bqs.easy.util;

import com.bqs.easy.httpclient.core.EasyHttpClient;

/**
 * 登陆接口，登陆的方法实现该方法<br>
 * 如果使用框架，那么使用第一个方法获得登陆实例<br>
 * 然后获得cookiestore,获得cookiestore后<br>
 * 将cookiestore转换成cookie，放到header中即可（未测试）
 * 
 * @author xym
 * @date 2015年8月4日
 * 
 */
public interface LoginUtilInterface {

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public EasyHttpClient Login(String name, String password);

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @param util
	 * @return
	 */
	public boolean Login(EasyHttpClient util, String name, String password);
}
