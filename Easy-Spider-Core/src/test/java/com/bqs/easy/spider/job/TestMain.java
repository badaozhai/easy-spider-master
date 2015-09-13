package com.bqs.easy.spider.job;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.impl.remover.BloomFilterDuplicateRemover;

public class TestMain {

	public static void main(String[] args) {
		BloomFilterDuplicateRemover r = new BloomFilterDuplicateRemover(new Task());
		for (int i = 0; i < 100; i++) {
			boolean b = r.isContainStr(i + "");
			System.out.println(b);
			if (!b) {
				r.putStr(i + "");
			}
		}
		for (int i = 0; i < 200; i++) {
			boolean b = r.isContainStr(i + "");
			System.out.println(b);
		}
		System.out.println(r.isContainStr(null));
		r.save();
	}
}
