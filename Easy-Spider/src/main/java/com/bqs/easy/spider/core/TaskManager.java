package com.bqs.easy.spider.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.job.SimpleJob;
import com.bqs.easy.util.MD5Util;

public class TaskManager {

	private static Log logger = LogFactory.getLog(TaskManager.class);

	private static TaskManager instance;

	private Set<Task> taskset = Collections.synchronizedSet(new LinkedHashSet<Task>());

	private Set<Task> runingtaskset = Collections.synchronizedSet(new LinkedHashSet<Task>());

	private static final String userdir = System.getProperty("user.dir") + File.separator;

	String taskdir = userdir + "tasks";

	private TaskManager() {
		loadTask();
	}

	public synchronized void addTask(Task t) {
		editTask(t, false);
	}

	public synchronized void delTask(Task t) {
		File f = getTaskFile(t);
		if (f.exists()) {
			if (!t.isDeleted()) {
				t.setDeleted(true);
				objectWirte(f, t, Task.class);
				QuartzManager.removeJob(t);
				logger.info("从更新队列中删除任务,id is : " + MD5Util.md5(t.getMainURL()));
			} else {
				taskset.remove(t);
				FileUtils.deleteQuietly(f);
				logger.info("彻底删除任务,id is : " + MD5Util.md5(t.getMainURL()));
			}
		} else {
			logger.warn("the task want to delete is no exits. id id : " + MD5Util.md5(t.getMainURL()));
		}
	}

	private File getTaskFile(Task t) {
		return new File(new File(taskdir), "task_" + MD5Util.md5(t.getMainURL()) + "__");
	}

	public synchronized void editTask(Task t, boolean isEdit) {
		if (taskset.contains(t)) {
			taskset.remove(t);
			taskset.add(t);
			if (!t.isDeleted()) {
				QuartzManager.modifyJobTime(t, t.getQuartzParam());
			}
		} else {
			taskset.add(t);
			if (!t.isDeleted()) {
				QuartzManager.addJob(t, SimpleJob.class, t.getQuartzParam());
			}
		}
		objectWirte(getTaskFile(t), t, Task.class);
		if (isEdit) {
			logger.info("编辑任务 [ " + t.getMainURL() + " ] success, id is " + MD5Util.md5(t.getMainURL()));
		} else {
			logger.info("添加任务 [ " + t.getMainURL() + " ] success, id is " + MD5Util.md5(t.getMainURL()));
		}
	}

	private void loadTask() {

		File taskdirfile = new File(taskdir);
		if (taskdirfile.exists()) {
			for (File taskfile : taskdirfile.listFiles()) {
				String name = taskfile.getName();
				if (name.startsWith("task_") && name.endsWith("__")) {
					Task t = objectRead(taskfile, Task.class);
					if (!taskset.contains(t)) {
						taskset.add(t);
						if (!t.isDeleted()) {
							QuartzManager.addJob(t, SimpleJob.class, t.getQuartzParam());
						}
					}
				}
			}
		} else {
			taskdirfile.mkdirs();
		}
		logger.info("加载任务完成，共加载任务 : [ " + taskset.size() + " ] 个");
	}

	/**
	 * task 读取
	 * 
	 * @param taskFile 任务
	 */
	@SuppressWarnings("unchecked")
	private <T> T objectRead(File taskFile, Class<T> c) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		T object = null;
		try {
			fis = new FileInputStream(taskFile);
			ois = new ObjectInputStream(fis);
			try {
				// 读出任务流.
				object = (T) ois.readObject();
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

	/**
	 * task 读取
	 * 
	 * @param taskFile 任务
	 */
	private <T> boolean objectWirte(File taskFile, T t, Class<T> c) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		boolean isSaved = true;
		try {
			fos = new FileOutputStream(taskFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(t); // 将对象流写入文件.
			oos.close();
		} catch (Exception e) {// 保存任务失败,磁盘空间不够导致保存任务失败
			logger.error("\u4fdd\u5b58\u4efb\u52a1\u5931\u8d25: " + e.getMessage());
			isSaved = false;
		} finally {
			try {
				oos.close();
				fos.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return isSaved;
	}

	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}

	public synchronized boolean isRunning(Task t) {
		if (runingtaskset.contains(t)) {
			return true;
		} else {
			runingtaskset.add(t);
			return false;
		}
	}

	public synchronized int runningTask() {
		return runingtaskset.size();
	}

	public synchronized void isDone(Task t) {
		runingtaskset.remove(t);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
		}
	}
}
