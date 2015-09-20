package com.bqs.easy.spider.impl.downloader;

import java.util.Set;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.log4j.Logger;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.bqs.easy.httpclient.core.EasyHttpClient;
import com.bqs.easy.httpclient.entity.Page;
import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.imp.IDownloader;

public class PhantomJsDownloader extends EasyHttpClient implements IDownloader {

	private static Logger log = Logger.getLogger(PhantomJsDownloader.class);

	PhantomJSDriver driver = null;

	public PhantomJsDownloader() {

		DesiredCapabilities caps = DesiredCapabilities.phantomjs();
		System.getProperties().setProperty("webdriver-logfile", "logs/phantomjsdriver.log");

		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				"D:\\360Download\\phantomjs-1.9.8-windows\\phantomjs.exe");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] { "--web-security=false",
				"--ssl-protocol=any", "--ignore-ssl-errors=true", "--disk-cache=false", "--load-images=false",
				"--webdriver-logfile=logs/phantomjsdriver.log", "--webdriver-loglevel=info" });
		driver = new PhantomJSDriver(caps);

		driver.get("http://news.baidu.com/");

	}

	public synchronized Page requestText(Request request) {

		String text = "";
		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] text start . ");
		try {
			String oldsission = driver.getWindowHandle();

			String url = request.getUrl();
			driver.get(url);
			text = driver.getPageSource();
			// 打开在新窗口
			driver.executeScript("window.open('')");

			Set<String> o = driver.getWindowHandles();

			System.out.println(o + "----- oldsission : " + oldsission);
			o.remove(oldsission);
			String[] sissions = new String[1];
			o.toArray(sissions);
			String newsission = sissions[0];
			PhantomJSDriver oldriver = (PhantomJSDriver) driver.switchTo().window(oldsission);
			oldriver.close();
			oldsission = newsission;
			driver = (PhantomJSDriver) driver.switchTo().window(newsission);
			log.info(request.getMethod() + " : [ " + request.getUrl() + " ]  text end . ");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		Page p = new Page();
		p.setHtml(text);
		p.setCharset(getCharset());
		p.setContent_type(getContent_type());
		p.setLastmodify(getLastmodify());
		p.setStatusCode(getStatusCode());

		return p;
	}

	public Page requestBytes(Request request) {

		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] bytes start . ");
		HttpUriRequest requesturi = requestHelp(request);
		byte[] bytes = execute_byte(requesturi, request.getHeader());
		log.info(request.getMethod() + " : [ " + request.getUrl() + " ] bytes end . ");
		Page p = new Page();
		p.setCharset(getCharset());
		p.setContent_type(getContent_type());
		p.setLastmodify(getLastmodify());
		p.setStatusCode(getStatusCode());
		p.setBytes(bytes);
		return p;
	}

	private HttpUriRequest requestHelp(Request request) {
		HttpUriRequest requesturi = getHttpUriRequest(request.getUrl(), request.getMethod(), request.getPostdata(),
				request.getHeader());
		return requesturi;
	}

	@Override
	public void close() {
		driver.quit();
	}
}
