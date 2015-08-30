package com.bqs.easy.spider.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bqs.easy.spider.core.TaskManager;
import com.bqs.easy.spider.entity.Task;

public class SimpleJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Task t = (Task) arg0.getJobDetail().getJobDataMap().get("task");
		TaskManager instance = TaskManager.getInstance();
		System.out.println("aaaaaaaaa");
		if (!instance.isRunning(t)) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(arg0.getNextFireTime() + "" + t);
			instance.isDone(t);
		} else {
			System.out.println("task is running.");
		}
	}

}
