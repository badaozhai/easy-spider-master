package com.bqs.easy.spider.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.util.MD5Util;

public class TaskManager {

	private static Log logger = LogFactory.getLog(TaskManager.class);

	private static TaskManager instance;

	private Set<Task> taskset = new LinkedHashSet<Task>();

	private static final String userdir = System.getProperty("user.dir") + File.separator;

	String taskdir = userdir + "tasks";

	private TaskManager() {
		loadTask();
	}

	public void addTask(Task t) {
		editTask(t, false);
	}

	public void delTask(Task t) {
		if (!t.isDeleted()) {

		} else {
			File f = getTaskFile(t);
			FileUtils.deleteQuietly(f);
		}
	}

	private File getTaskFile(Task t) {
		return new File(new File(taskdir), "task_" + MD5Util.md5(t.getMainURL()) + "__");
	}

	public void editTask(Task t, boolean isEdit) {
		if (taskset.contains(t)) {
			taskset.remove(t);
			taskset.add(t);
		} else {
			taskset.add(t);
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
	 * @param taskFile
	 *            任务
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
	 * @param taskFile
	 *            任务
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

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
		}
	}
}
