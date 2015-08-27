/**
 * Copyright (C) 2015 xym, Inc. All Rights Reserved.
 */
package com.bqs.loginutil.test.renminyinhang;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bqs.easy.core.EasyHttpClient;

/**
 * 人民银行测试
 * 
 * @author xym
 * @date 2015年8月20日
 *
 */
public class RenMinYinHangTest {

	public static void main(String[] args) {
		EasyHttpClient util = new EasyHttpClient();
		String s = util.Get("http://www.pbc.gov.cn/goutongjiaoliu/113456/2164857/index.html");
		String randomStr = EasyHttpClient.GetText(s, "RANDOMSTR(\\d*)");// 通过正则得到数字
		String templateStr = EasyHttpClient.GetText(s, "WZWS_CONFIRM_PREFIX_LABEL(\\d*)");// 通过正则得到数字
		String cookiestr = CookieUtil.QWERTASDFGXYSF(Integer.parseInt(templateStr), "RANDOMSTR" + randomStr, "STRRANDOM" + randomStr);

		String ss = "";
		String wzwschallenge = CookieUtil.KTKY2RBD9NHPBCIHV9ZMEQQDARSLVFDU(cookiestr);
		ss = "wzwschallenge=" + wzwschallenge + "; path=/" + ";";
		ss = ss + "wzwstemplate=" + CookieUtil.KTKY2RBD9NHPBCIHV9ZMEQQDARSLVFDU(templateStr) + "; path=/" + ";";
		Map<String, String> header = new LinkedHashMap<String, String>();
		header.put("cookie", ss);
		s = util.Get("http://www.pbc.gov.cn/goutongjiaoliu/113456/2164857/index.html", header, "");
		System.out.println(s);
		
		util.close();

	}
}
