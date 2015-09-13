package com.bqs.easy.spider.job;

import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.core.MyClassLoader;
import com.bqs.easy.spider.imp.IDownloader;

public class ReflectTest {

	public static void main(String[] args) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		IDownloader d = MyClassLoader.load("com.bqs.easy.spider.impl.downloader.HttpClientDownloader", cl,
				IDownloader.class);
		System.out.println(d.requestText(new Request("http://coolszy.iteye.com/blog/569846")));
	}
}
