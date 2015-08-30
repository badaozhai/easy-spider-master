package com.bqs.easy.spider.job;

import com.bqs.easy.spider.core.TaskManager;
import com.bqs.easy.spider.entity.Task;

public class Main {

	public static void main(String[] args) {
		TaskManager instance=TaskManager.getInstance();
		instance.addTask(new Task());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		instance.delTask(new Task());
		instance.delTask(new Task());
	}
}
