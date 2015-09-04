package com.bqs.easy.spider.job;

import com.bqs.easy.spider.downloader.MyHttpClient;

public class Main {

	public static void main(String[] args) {
		MyHttpClient h=new MyHttpClient();
		h.Get("https://www.baidu.com/");
		System.out.println(h.getContent_type());
		System.out.println(h.getContent_type());
	}
}
