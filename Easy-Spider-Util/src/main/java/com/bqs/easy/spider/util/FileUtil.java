package com.bqs.easy.spider.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {

	private static Log logger = LogFactory.getLog(FileUtil.class);

	/**
	 * task 读取
	 * 
	 * @param taskFile 任务
	 */
	@SuppressWarnings("unchecked")
	public static <T> T objectRead(File taskFile, Class<T> c) {
		if (!taskFile.exists()) {
			return null;
		}
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
				// 这里出现过异常，出现原因可能是bloomfilter写入过程中关闭程序
				logger.error(ex.getMessage(), ex);
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
	public static <T> boolean objectWirte(File taskFile, T t, Class<T> c) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		boolean isSaved = true;

		try {
			File parent = taskFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
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
}
