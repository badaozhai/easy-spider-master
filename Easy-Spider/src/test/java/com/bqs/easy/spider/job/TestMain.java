package com.bqs.easy.spider.job;

import com.bqs.easy.spider.downloader.HttpClientDownloader;

public class TestMain {

	public static void main(String[] args) {
		HttpClientDownloader h=new HttpClientDownloader();
		h.Get("http://so.tv.sohu.com/mts?&c=&f=js&wd=%u738b%u9633%u660e");
		System.out.println(h.getContent_type());
		System.out.println(h.getContent_type());
		h.close();
	}
}
