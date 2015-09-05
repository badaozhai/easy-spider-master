package com.bqs.easy.spider.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.bqs.easy.httpclient.entity.Page;
import com.bqs.easy.spider.imp.IPipeline;

public class ConsolePipeline implements IPipeline {

	private static Logger log = Logger.getLogger(ConsolePipeline.class);
	List<Object> list = Collections.synchronizedList(new ArrayList<Object>());

	@Override
	public Object process(Page p) {
		return p.getHtml().length();
	}

	@Override
	public void save() {
		for (Object object : list) {
			log.info(object);
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
