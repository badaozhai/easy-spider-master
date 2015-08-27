package com.bqs.loginutil.test.renminyinhang;

/**
 * 人民银行cookie处理
 * 
 * @author xym
 * @date 2015年6月25日
 */
public class CookieUtil {

	static String encoderchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

	static int zhishu[] = { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 };

	public static String KTKY2RBD9NHPBCIHV9ZMEQQDARSLVFDU(String str) {// 这个方法是计算出一个值的方法
		String out;
		int i;
		int len;
		int c1, c2, c3;
		len = str.length();
		i = 0;
		out = "";
		while (i < len) {
			c1 = str.charAt(i++) & 0xff;
			if (i == len) {
				out += encoderchars.charAt(c1 >> 2);// charAt 是取encoderchars的第几个字符，超过范围报错？返回undifine？
				out += encoderchars.charAt((c1 & 0x3) << 4);
				out += "==";
				break;
			}
			c2 = str.charAt(i++);
			if (i == len) {
				out += encoderchars.charAt(c1 >> 2);
				out += encoderchars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xf0) >> 4));
				out += encoderchars.charAt((c2 & 0xf) << 2);
				out += "=";
				break;
			}
			c3 = str.charAt(i++);
			out += encoderchars.charAt(c1 >> 2);
			out += encoderchars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xf0) >> 4));
			out += encoderchars.charAt(((c2 & 0xf) << 2) | ((c3 & 0xc0) >> 6));
			out += encoderchars.charAt(c3 & 0x3f);
		}
		return out;
	}

	public static String QWERTASDFGXYSF(int w, String wzwschallenge, String wzwschallengex) {// 计算hash
		String tmp = wzwschallenge + wzwschallengex;
		int hash = 0;
		int i = 0;
		for (i = 0; i < tmp.length(); i++) {
			hash += tmp.charAt(i);
		}
		hash *= zhishu[w - 1];
		hash += 111111;
		return "WZWS_CONFIRM_PREFIX_LABEL" + w + hash;
	}

	//
	public void test() {
	}

	static String wzwschallenge = "RANDOMSTR23490";
	static String wzwschallengex = "STRRANDOM23490";
	static int template = 7;

	// WZWS_CONFIRM_PREFIX_LABEL6142085
	public static void main(String[] args) {
		System.out.println(KTKY2RBD9NHPBCIHV9ZMEQQDARSLVFDU(template + ""));
		System.out.println(KTKY2RBD9NHPBCIHV9ZMEQQDARSLVFDU(QWERTASDFGXYSF(template, wzwschallenge, wzwschallengex)));
	}
}
