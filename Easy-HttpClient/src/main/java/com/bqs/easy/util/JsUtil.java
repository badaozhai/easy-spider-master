/**
 * Copyright (C) 2015 xym, Inc. All Rights Reserved.
 */
package com.bqs.easy.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;

import com.bqs.easy.httpclient.core.EasyHttpClient;

/**
 * 进行js操作的帮助类
 * 
 * @author xym
 * @date 2015年8月27日
 *
 */
public class JsUtil {

	private static Logger log = Logger.getLogger(JsUtil.class);

	/**
	 * 读取打包的js代码
	 * 
	 * @param filepath
	 * @return
	 */
	public static String readJs(String filepath) {
		String a = "";
		BufferedReader br = null;

		InputStream is = null;
		if (!filepath.startsWith("/")) {
			filepath = "/" + filepath;
		}
		String path = EasyHttpClient.class.getResource(filepath).toString();
		log.info("readJs In Path : " + path);
		try {
			is = EasyHttpClient.class.getResourceAsStream(filepath);

			StringBuilder strBlder = new StringBuilder("");
			br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while (null != (line = br.readLine())) {
				strBlder.append(line + "\n");
			}
			a = strBlder.toString();
			br.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return a;
	}

	/**
	 * 执行js的方法
	 * 
	 * @param js
	 *            js代码
	 * @param function
	 *            想要执行的js方法
	 * @param jsform
	 *            执行js的参数
	 * @return
	 * @throws Exception
	 */
	public static String runJs(String js, String function, Object[] jsform) throws Exception {

		ScriptEngineManager manager = new ScriptEngineManager();
		// 获得js引擎
		ScriptEngine engine = manager.getEngineByName("js");
		// 将js代码注入引擎
		engine.eval(js);

		Invocable jsInvoke = (Invocable) engine;
		// 执行js方法
		Object result1 = jsInvoke.invokeFunction(function, jsform);
		// 返回结果
		return result1.toString();
	}
}
