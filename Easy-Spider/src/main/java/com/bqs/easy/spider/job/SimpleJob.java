package com.bqs.easy.spider.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bqs.easy.spider.entity.Task;

public class SimpleJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Task t=(Task) arg0.getJobDetail().getJobDataMap().get("task");
		System.out.println(arg0.getNextFireTime()+""+t);
	}

}
