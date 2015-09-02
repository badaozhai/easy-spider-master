package com.bqs.easy.spider.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bqs.easy.spider.core.SpiderConfig;
import com.bqs.easy.spider.core.TaskManager;
import com.bqs.easy.spider.entity.Task;

public class SimpleJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Task t = (Task) arg0.getJobDetail().getJobDataMap().get("task");
		TaskManager instance = TaskManager.getInstance();
		System.out.println(t.getMainURL());
		if (!instance.isRunning(t)) {
			if(instance.runningTask()<10){
				SpiderConfig spiderConfig = new SpiderConfig(t);
				new Thread(spiderConfig).start();
			}else{
				System.out.println("max size");
			}
		} else {
			System.out.println("task is running.");
		}
	}

}
