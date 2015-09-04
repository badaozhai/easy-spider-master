/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.imp;

import com.bqs.easy.httpclient.entity.Request;

/**
 * @author xym
 * @date 2015年9月4日
 *
 */
public interface IDownloader {

	public String requestText(Request request);

	public byte[] requestBytes(Request request);

	/**
	 * @return 网页的content_type
	 */
	public String getContent_type();

	/**
	 * @return 网页的字符集
	 */
	public String getCharset();

	/**
	 * @return 网页最后更新时间
	 */
	public long getLastmodify();

	/**
	 * @return 网页返回状态
	 */
	public int getStatusCode();

}
