/**
 * Copyright (C) 2015 BQshe, Inc. All Rights Reserved.
 */
package com.bqs.easy.spider.core;

import java.io.Closeable;
import java.io.IOException;

import com.bqs.easy.spider.entity.Task;

/**
 * @author xym
 * @date 2015年9月2日
 *
 */
public class SpiderConfig implements Runnable,Closeable{

	private Task t=null;
	public SpiderConfig(Task t){
		this.t=t;
	}
	
	@Override
	public void run() {
		System.out.println(t.getMainURL());
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		TaskManager.getInstance().isDone(t);
	}

}
