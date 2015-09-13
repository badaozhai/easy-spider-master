package com.bqs.easy.spider.common;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;

import com.bqs.easy.spider.manager.QuartzManager;

public class StopSpider implements Runnable {

	Logger logger = Logger.getLogger(StopSpider.class);
	Tomcat TOMCAT = null;

	public StopSpider(Tomcat TOMCAT) {
		this.TOMCAT = TOMCAT;
	}

	@Override
	public void run() {

		QuartzManager.shutdownJobs();
		logger.info("手动停止spider");
		// 停止 Quartz 定时器
		if (TOMCAT != null) {
			try {
				TOMCAT.stop();
				logger.info("停止TOMCAT");
			} catch (LifecycleException e) {
				logger.error(e.getMessage());
			}
		}

	}
}
