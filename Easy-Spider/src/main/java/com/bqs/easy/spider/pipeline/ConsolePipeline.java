package com.bqs.easy.spider.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import com.bqs.easy.httpclient.entity.Page;
import com.bqs.easy.spider.imp.IPipeline;

public class ConsolePipeline implements IPipeline {

	private static Logger log = Logger.getLogger(ConsolePipeline.class);
	List<Object> list = Collections.synchronizedList(new ArrayList<Object>());

	@Override
	public Object process(Page p) {
		return Jsoup.parse(p.getHtml()).text().replaceAll("[\n\t]", "");
	}

	@Override
	public void save() {
		log.info("save");
		try {
			FileUtils.writeLines(new File("data"+File.separator+System.currentTimeMillis()+".txt"), list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void add(Object o) {
		list.add(o);
		if (list.size() >= 100) {
			save();
			list.clear();
		}
	}

}
