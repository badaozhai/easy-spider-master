package com.bqs.easy.spider.job;

import com.bqs.easy.spider.core.QuartzManager;
import com.bqs.easy.spider.entity.Task;

public class Main {

	public static void main(String[] args) {
		QuartzManager.addJob(new Task(), SimpleJob.class, "0/10 0/1 * * * ?");
	}
}
