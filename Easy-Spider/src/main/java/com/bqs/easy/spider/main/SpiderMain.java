package com.bqs.easy.spider.main;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

import com.bqs.easy.spider.common.StopSpider;
import com.bqs.easy.spider.manager.TaskManager;

/**
 * Hello world!
 *
 */
public class SpiderMain {

	public static final String USERDIR = System.getProperty("user.dir") + File.separator;

	public static void main(String[] args) {
		TaskManager.getInstance();

		System.setProperty("catalina.home", USERDIR);
		Tomcat tomcat = new Tomcat();

		tomcat.setPort(8080);

		tomcat.getConnector().setAsyncTimeout(20000L);
		String appBase = USERDIR + "webapps" + File.separator + "ROOT";
		tomcat.setBaseDir(USERDIR);

		StandardServer server = (StandardServer) tomcat.getServer();
		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);
		try {
			tomcat.addWebapp("", appBase);
			tomcat.start();

			Runtime.getRuntime().addShutdownHook(new Thread(new StopSpider(tomcat)));
		} catch (ServletException ex) {
		} catch (LifecycleException ex) {
		}

		tomcat.getServer().await();
	}
}
