package com.bqs.easy.spider.job;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.manager.TaskManager;

public class Main {

	public static void main(String[] args) {
		TaskManager instance=TaskManager.getInstance();
		Task t=new Task();
		instance.addTask(t);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		instance.delTask(t);
//		instance.delTask(t);
//		System.exit(0);
	}
}
