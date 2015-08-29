package com.bqs.easy.spider.job;

import com.bqs.easy.spider.core.QuartzManager;

public class Main {

	public static void main(String[] args) {
		QuartzManager.addJob("test", SimpleJob.class, "0/10 0/1 * * * ?");
		QuartzManager.startJobs();
	}
}
