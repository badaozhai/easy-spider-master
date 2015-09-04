package com.bqs.easy.spider.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bqs.easy.spider.core.Spider;
import com.bqs.easy.spider.core.SpiderConfig;
import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.manager.TaskManager;

public class RunTaskJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Task t = (Task) arg0.getJobDetail().getJobDataMap().get("task");
		if (!TaskManager.FIRST_FIFO.contains(t)) {
			if (TaskManager.FIRST_FIFO.size() < 10) {
				SpiderConfig spiderConfig = new SpiderConfig(t);
				new Spider(spiderConfig).start();
			} else {
				if (TaskManager.SECOND_FIFO.contains(t)) {
					System.out.println("任务已经在第二队列中" + t.getMainURL());
				} else {
					System.out.println("将任务放到第二队列" + t.getMainURL());
					TaskManager.SECOND_FIFO.add(t);
				}
			}
		} else {
			System.out.println("task is running." + t.getMainURL());
		}
	}

}
