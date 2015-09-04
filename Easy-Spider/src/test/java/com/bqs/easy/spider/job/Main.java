package com.bqs.easy.spider.job;

import com.bqs.easy.spider.downloader.HttpClientDownloader;

public class Main {

	public static void main(String[] args) {
		HttpClientDownloader h=new HttpClientDownloader();
		h.Get("https://www.baidu.com/");
		System.out.println(h.getContent_type());
		System.out.println(h.getContent_type());
	}
}
