package com.bqs.easy.spider.core;

import org.apache.log4j.Logger;

public class MyClassLoader {

	private static Logger log = Logger.getLogger(MyClassLoader.class);

	@SuppressWarnings("unchecked")
	public static <T> T load(String cx, ClassLoader cl, Class<T> cc) {

		T t = null;
		try {
			Class<?> c = null;
			c = (Class<?>) cl.loadClass(cx);// 从加载器中加载Class
			t = (T) c.newInstance();
		} catch (ClassNotFoundException e) {
			String error = "java.lang.ClassNotFoundException : ";
			log.error(error + cc.getCanonicalName() + " user package is : [ " + cx + " ]");
		} catch (InstantiationException e) {
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		}
		return t;
	}

}
