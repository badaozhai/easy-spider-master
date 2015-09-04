package com.bqs.easy.spider.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bqs.easy.spider.core.Spider;
import com.bqs.easy.spider.core.SpiderConfig;
import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.manager.TaskManager;

public class RunTaskJob implements Job {

	private static Logger log = Logger.getLogger(RunTaskJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Task t = (Task) arg0.getJobDetail().getJobDataMap().get("task");
		if (!TaskManager.FIRST_FIFO.contains(t)) {
			if (TaskManager.FIRST_FIFO.size() < 10) {
				SpiderConfig spiderConfig = new SpiderConfig(t);
				new Spider(spiderConfig).start();
			} else {
				if (TaskManager.SECOND_FIFO.contains(t)) {
					log.info("任务已经在第二队列中" + t.getMainURL());
				} else {
					log.info("将任务放到第二队列" + t.getMainURL());
					TaskManager.SECOND_FIFO.add(t);
				}
			}
		} else {
			log.info("task is running." + t.getMainURL());
		}
	}

}
