package com.bqs.easy.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * base64加密，解密
 * 
 * @author xym
 * @date 2015年8月27日
 *
 */
@SuppressWarnings("restriction")
public class Base64Util {
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		return getFromBASE64(s, "utf-8");
	}

	// 将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s, String charset) {
		if (s == null) {
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b, charset);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String str = "0123";
		String bb = Base64Util.getBase64(str);
		System.out.println(bb);
		System.out.println(Base64Util.getFromBASE64(bb));
	}
}
