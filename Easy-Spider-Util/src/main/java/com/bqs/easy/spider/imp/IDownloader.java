/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.imp;

import com.bqs.easy.httpclient.entity.Page;
import com.bqs.easy.httpclient.entity.Request;

/**
 * @author xym
 * @date 2015年9月4日
 *
 */
public interface IDownloader {

	public Page requestText(Request request);

	public Page requestBytes(Request request);

	public void close();

}
