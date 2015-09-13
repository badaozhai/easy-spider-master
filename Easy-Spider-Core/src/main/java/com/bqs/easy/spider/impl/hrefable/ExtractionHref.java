package com.bqs.easy.spider.impl.hrefable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.imp.IExtractionHrefAble;
import com.bqs.easy.spider.impl.downloader.HttpClientDownloader;
import com.bqs.easy.util.MyStringUtil;


/**
 * 通用连接提取方法，可以自定义
 * @author xym
 * @date 2015年9月3日
 */
public class ExtractionHref implements IExtractionHrefAble {
	private static Logger log = Logger.getLogger(HttpClientDownloader.class);

	/**
	 * 连接提取
	 * @param referer 来源url
	 * @param html 网页内容
	 * @param taskmap
	 * @param charset 网页字符集
	 * @return
	 */
	public List<Request> parserLinksInHTML(String referer, String html, Map<String, Map<String, String>> taskmap,
			String charset) {
		List<Request> list = new ArrayList<Request>();
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("a");
		for (Element element : links) {
			String href = element.attr("href").trim();
			String title = element.text().trim();
			if ("#".equals(href) || "".equals(href) || href.contains("javascript")) {
				continue;
			}
			if (!href.startsWith("http")) {
				href = MyStringUtil.tidyUrl(href, referer);
			}
			if (href.contains("#")) {
				href = href.substring(0, href.indexOf("#"));
			}

			Request r = new Request(href);
			r.setTitle(title);
			r.setReferer(referer);
			if (list.contains(r)) {
				if (!"".equals(title)) {
					list.remove(r);
					list.add(r);
				} else {
				}
			} else {
				list.add(r);
			}
		}

		log.info("url size=" + list.size());
		return list;
	}

	public static void main(String[] args) {
		HttpClientDownloader h = new HttpClientDownloader();
		ExtractionHref p = new ExtractionHref();
		String referer = "http://news.baidu.com/ns?cl=3&ct=9&rn=20&sp=hotquery&tn=news&word=%CF%B0%BD%FC%C6%BD%20%BD%B2%BB%B0%20%B2%C3%BE%FC30%CD%F2";
		String html = h.Get(referer);
		p.parserLinksInHTML(referer, html, null, h.getCharset());

		Map<String, String> hf = new LinkedHashMap<String, String>();
		Map<String, String> hf1 = new LinkedHashMap<String, String>();
		System.out.println(hf);
		System.out.println(hf1);
		System.out.println(hf1 == hf);
		System.out.println(hf1.equals(hf));
		Request r = new Request();
		Request r1 = new Request();
		System.out.println(r1 == r);
		System.out.println(r1.equals(r));
		List<Request> l = new ArrayList<Request>();
		l.add(r1);
		System.out.println(l.contains(r));
	}
}