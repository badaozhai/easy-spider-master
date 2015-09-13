package com.bqs.easy.spider.impl.remover;

import com.bqs.easy.httpclient.entity.Request;
import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.imp.IDuplicateRemover;
import com.bqs.easy.spider.util.FileUtil;
import com.bqs.easy.util.MD5Util;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 * @author xym
 * @date 2015-09-06
 */
public class BloomFilterDuplicateRemover extends IDuplicateRemover {

	private static final String userdir = System.getProperty("user.dir") + File.separator;

	String bloomfilterfilepath = userdir + "blf";

	private int expectedInsertions;

	private BloomFilter<CharSequence> bloomFilter;

	private double fpp;

	private Task t;

	/**
	 * 根据任务获得bloomFilter文件的名称
	 * 
	 * @param t
	 * @return
	 */
	private File getTaskFile(Task t) {
		return new File(new File(bloomfilterfilepath), "blf_" + MD5Util.md5(t.getMainURL()) + "__");
	}

	public BloomFilterDuplicateRemover(Task t) {
		super(t);
		this.t = t;
		load(1000000);
	}

	@SuppressWarnings("unchecked")
	public void load(int expectedInsertions) {
		bloomFilter = FileUtil.objectRead(getTaskFile(t), BloomFilter.class);
		if (bloomFilter == null) {
			load(expectedInsertions, 0.01);
		}
	}

	/**
	 *
	 * @param expectedInsertions
	 *            the number of expected insertions to the constructed
	 * @param fpp
	 *            the desired false positive probability (must be positive and
	 *            less than 1.0)
	 */
	public void load(int expectedInsertions, double fpp) {
		this.expectedInsertions = expectedInsertions;
		this.fpp = fpp;
		this.bloomFilter = rebuildBloomFilter();
	}

	protected BloomFilter<CharSequence> rebuildBloomFilter() {
		return BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions, fpp);
	}

	public boolean isContains(Request request) {
		boolean isDuplicate = bloomFilter.mightContain(getMd5(getUrl(request)));
		return isDuplicate;
	}

	public void put(Request request) {
		String url = getUrl(request);

		boolean isDuplicate = bloomFilter.mightContain(getMd5(url));
		if (!isDuplicate) {
			t.getCounter().incrementAndGet();
			if(t.getCounter().get()%50==0){
				this.save();
			}
			bloomFilter.put(getMd5(url));
		}
	}

	/**
	 * 将request对象转换成url<br>
	 * 如果对象为空，返回“”<br>
	 * 如果对象的提交方式是get，返回url<br>
	 * 如果对象的提交方式是post，那么将postdata拼到url后返回
	 * 
	 * @param request
	 * @return
	 */
	private String getUrl(Request request) {
		String url = "";
		if (request != null && null != request.getUrl()) {
			String method = request.getMethod();
			if ("post".equalsIgnoreCase(method)) {
				url = request.getUrl();
				Map<String, String> postdata = request.getPostdata();
				if (postdata != null && postdata.size() > 0) {
					url = url + "?";
					for (String str : postdata.keySet()) {
						url += str + "=" + postdata.get(str);
					}
				}
			} else {
				url = request.getUrl();
			}
		}
		return url;
	}

	public boolean isContainStr(String url) {
		boolean isDuplicate = bloomFilter.mightContain(getMd5(url));
		return isDuplicate;
	}

	public void putStr(String url) {
		boolean isDuplicate = bloomFilter.mightContain(getMd5(url));
		if (!isDuplicate) {
			t.getCounter().incrementAndGet();
			bloomFilter.put(getMd5(url));
		}
	}

	/**
	 * 将url进行MD5
	 * 
	 * @param url
	 * @return
	 */
	protected String getMd5(String url) {
		if (url == null) {
			return MD5Util.md5("");
		}
		return MD5Util.md5(url);
	}

	public void reset() {
		rebuildBloomFilter();
	}

	public int size() {
		return t.getCounter().get();
	}

	public void close() {

	}

	public void load(Task t) {
		this.t = t;
	}

	public void save() {
		FileUtil.objectWirte(getTaskFile(t), bloomFilter, BloomFilter.class);
	}

}
