package com.bqs.easy.spider.imp;

import com.bqs.easy.httpclient.entity.Page;

public interface IPipeline {

	public Object process(Page p);

	public void save();

	public void add(Object o);
}
