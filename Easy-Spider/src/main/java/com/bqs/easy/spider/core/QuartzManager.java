package com.bqs.easy.spider.core;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 定时任务管理类
 * @date 2014-6-26 下午03:15:52
 */
public class QuartzManager {

	private static Logger log = LoggerFactory.getLogger(QuartzManager.class);

	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

	/**
	 * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName 任务名
	 * @param cls 任务
	 * @param time 时间设置，参考quartz说明文档
	 */
	@SuppressWarnings("rawtypes")
	public static void addJob(String jobName, Class cls, String time) {
		addJob(jobName, cls, time, false);
	}

	/**
	 * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName 任务名
	 * @param cls 任务
	 * @param time 时间设置，参考quartz说明文档
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addJob(String jobName, Class cls, String time, boolean isedit) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();

			JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();
			// 触发器
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)// 触发器名,触发器组
					.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();// 触发器时间设定
			Date date = sched.scheduleJob(jobDetail, trigger);
			if (isedit) {
				log.info("Edit Task " + jobDetail.getKey() + " Will Run @ " + date);
			} else {
				log.info("The Task " + jobDetail.getKey() + " First Run @ " + date);
			}
			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 添加一个定时任务
	 * 
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @param triggerName 触发器名
	 * @param triggerGroupName 触发器组名
	 * @param jobClass 任务
	 * @param time 时间设置，参考quartz说明文档
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).build();
			// 触发器
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)// 触发器名,触发器组
					.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();// 触发器时间设定
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 */
	@SuppressWarnings("rawtypes")
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();

			TriggerKey triggerkey = new TriggerKey(jobName, TRIGGER_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerkey);

			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobKey jobkey = new JobKey(jobName, JOB_GROUP_NAME);
				JobDetail jobDetail = sched.getJobDetail(jobkey);
				Class objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time, true);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 修改一个任务的触发时间
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 */
	@SuppressWarnings("rawtypes")
	public static void modifyJobTime(String jobName, String triggerGroupName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			TriggerKey triggerkey = new TriggerKey(jobName, triggerGroupName);
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerkey);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobKey jobkey = new JobKey(jobName, triggerGroupName);
				JobDetail jobDetail = sched.getJobDetail(jobkey);
				Class objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time, true);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * @param jobName
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			TriggerKey key = new TriggerKey(jobName, TRIGGER_GROUP_NAME);
			JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
			sched.pauseTrigger(key);// 停止触发器
			sched.unscheduleJob(key);// 移除触发器
			sched.deleteJob(jobKey);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 * 
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			TriggerKey key = new TriggerKey(triggerName, triggerGroupName);
			JobKey jobKey = new JobKey(jobName, jobGroupName);
			sched.pauseTrigger(key);// 停止触发器
			sched.unscheduleJob(key);// 移除触发器
			sched.deleteJob(jobKey);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:启动所有定时任务
	 * 
	 * 
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:关闭所有定时任务
	 * 
	 * 
	 * @Title: QuartzManager.java
	 * @Copyright: Copyright (c) 2014
	 * 
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}