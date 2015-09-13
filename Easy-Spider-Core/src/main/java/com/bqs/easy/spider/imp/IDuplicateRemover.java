package com.bqs.easy.spider.imp;

import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.entity.Task;

public abstract class IDuplicateRemover {

	public IDuplicateRemover(Task t) {

	}

	public abstract boolean isContains(Request request);

	public abstract boolean isContainStr(String url);

	public abstract void put(Request request);

	public abstract void putStr(String url);

	public abstract int size();

	public abstract void reset();

	public abstract void save();

	public abstract void close();
}
