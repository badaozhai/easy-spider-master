package com.bqs.easy.spider.imp;

import java.util.List;
import java.util.Map;

import com.bqs.easy.httpclient.entity.Request;

/**
 * 连接提取的父类
 * 
 * @author xym
 * @date 2015年9月3日
 */
public interface IExtractionHrefAble {

	/**
	 * 连接提取
	 * @param referer 来源url
	 * @param html 网页内容
	 * @param taskmap
	 * @param charset 网页字符集
	 * @return
	 */
	public List<Request> parserLinksInHTML(String referer, String html, Map<String, Map<String, String>> taskmap,
			String charset);
}