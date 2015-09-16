package com.bqs.easy.spider.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

public class MyClassLoader {

	private static Logger log = Logger.getLogger(MyClassLoader.class);

	@SuppressWarnings("unchecked")
	public static <T> T load(String classname, ClassLoader classloader, Class<T> classType, Class<T>[] paramTypes,
			Object[] params) {

		log.info("classname : [ " + classname + " ] . classloader : [ " + classloader + " ] . classType : [ "
				+ classType + " ] .");
		if (null == classname || null == classloader || null == classType) {
			log.error("classname : [ " + classname + " ] . classloader : [ " + classloader + " ] . classType : [ "
					+ classType + " ] .");
			return null;
		}
		T t = null;
		try {
			Class<?> c = null;

			c = (Class<?>) classloader.loadClass(classname);// 从加载器中加载Class

			if (paramTypes == null || paramTypes.length == 0) {// 无参构造方法
				t = (T) c.newInstance();
			} else {
				Constructor<?> con = c.getConstructor(paramTypes); // 有参构造方法
				t = (T) con.newInstance(params);
			}

		} catch (ClassNotFoundException e) {
			String error = "java.lang.ClassNotFoundException : ";
			log.error(error + classType.getCanonicalName() + " user package is : [ " + classname + " ]");
		} catch (InstantiationException e) {
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage());
		} catch (SecurityException e) {
			log.error(e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
		} catch (InvocationTargetException e) {
			log.error(e.getMessage());
		}
		return t;
	}

	public static <T> T load(String classname, ClassLoader classloader, Class<T> classType) {
		return load(classname, classloader, classType, null, null);
	}

}
