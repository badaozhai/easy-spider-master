package com.bqs.easy.spider.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.job.RunTaskJob;
import com.bqs.easy.spider.util.FileUtil;
import com.bqs.easy.util.MD5Util;

public class TaskManager {

	private static Log logger = LogFactory.getLog(TaskManager.class);

	private static TaskManager instance;

	/**
	 * 任务列表，包含所有 任务
	 */
	private Set<Task> taskset = Collections.synchronizedSet(new LinkedHashSet<Task>());

	/**
	 * 任务第一队列（正在运行），这个队列只有10个任务，超过10个进入第二队列
	 */
	public static List<Task> FIRST_FIFO = Collections.synchronizedList(new ArrayList<Task>());
	/**
	 * 第二队列，等待队列
	 */
	public static List<Task> SECOND_FIFO = Collections.synchronizedList(new ArrayList<Task>());

	private static final String userdir = System.getProperty("user.dir") + File.separator;

	String taskdir = userdir + "tasks";

	private TaskManager() {
		loadTask();
	}

	public synchronized void addTask(Task t) {
		editTask(t, false);
	}

	/**
	 * 删除任务
	 * 
	 * @param t
	 */
	public synchronized void delTask(Task t) {
		File f = getTaskFile(t);
		if (f.exists()) {// 判断磁盘上任务是否存在//TODO 会不会出现磁盘不存在，但是实际存在的任务？
			if (!t.isDeleted()) {// 不带有删除标记的任务，先设置删除标记，然后从更新队列中删除
				t.setDeleted(true);
				FileUtil.objectWirte(f, t, Task.class);
				QuartzManager.removeJob(t);
				logger.info("从更新队列中删除任务,id is : " + MD5Util.md5(t.getMainURL()));
			} else {// 带有删除标记的任务，从任务列表里删除，删除文件
				taskset.remove(t);
				FileUtils.deleteQuietly(f);
				logger.info("彻底删除任务,id is : " + MD5Util.md5(t.getMainURL()));
			}
		} else {
			logger.warn("the task want to delete is no exits. id id : " + MD5Util.md5(t.getMainURL()));
		}
	}

	/**
	 * 根据任务获得任务写在磁盘的文件名
	 * 
	 * @param t
	 * @return
	 */
	private File getTaskFile(Task t) {
		return new File(new File(taskdir), "task_" + MD5Util.md5(t.getMainURL()) + "__");
	}

	/**
	 * 编辑任务
	 * 
	 * @param t
	 *            任务
	 * @param isEdit
	 *            任务状态是否属实编辑状态
	 */
	public synchronized void editTask(Task t, boolean isEdit) {
		String url = t.getMainURL();
		if (!url.matches("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?")) {
			logger.error("URL不合法");
			return;
		}
		if (taskset.contains(t)) {// 如果原先已经存在该任务，那么删除该任务，重新添加
			taskset.remove(t);
			taskset.add(t);
			if (!t.isDeleted()) {// 如果任务不带有删除标记，那么更新任务更新时间
				QuartzManager.modifyJobTime(t, t.getQuartzParam());
			}
		} else {
			taskset.add(t);
			if (!t.isDeleted()) {
				QuartzManager.addJob(t, RunTaskJob.class, t.getQuartzParam());
			}
		}
		FileUtil.objectWirte(getTaskFile(t), t, Task.class);
		if (isEdit) {
			logger.info("编辑任务 [ " + t.getMainURL() + " ] success, id is " + MD5Util.md5(t.getMainURL()));
		} else {
			logger.info("添加任务 [ " + t.getMainURL() + " ] success, id is " + MD5Util.md5(t.getMainURL()));
		}
	}

	/**
	 * 重磁盘加载任务
	 */
	private void loadTask() {

		File taskdirfile = new File(taskdir);
		if (taskdirfile.exists()) {// 如果任务目录存在，加载目录下的任务
			for (File taskfile : taskdirfile.listFiles()) {
				String name = taskfile.getName();
				if (name.startsWith("task_") && name.endsWith("__")) {
					Task t = FileUtil.objectRead(taskfile, Task.class);
					if (!taskset.contains(t)) {
						taskset.add(t);
						if (!t.isDeleted()) {
							QuartzManager.addJob(t, RunTaskJob.class, t.getQuartzParam());
						}
					}
				}
			}
		} else {// 如果目录不存在，创建任务目录
			taskdirfile.mkdirs();
		}
		logger.info("加载任务完成，共加载任务 : [ " + taskset.size() + " ] 个");
	}


	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
		}
	}
}
