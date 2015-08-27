/**
 * 
 */
package com.bqs.loginutil.test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bqs.easy.core.EasyHttpClient;
import com.bqs.easy.util.LoginUtilInterface;
import com.bqs.easy.util.MD5Util;
import com.bqs.easy.util.RuoKuai;

/**
 * 91旺财登陆
 * 
 * @author xym
 * @date 2015年5月29日
 */
public class _91WangCaiLoginUtil implements LoginUtilInterface {

	private static Logger log = Logger.getLogger(_91WangCaiLoginUtil.class);

	/**
	 * 
	 * @param util
	 * @param imgsrc 图片的地址
	 * @param postdata 请求的数据
	 * @param trytimes 最大重试次数
	 * @return
	 */
	public static String TryIt(EasyHttpClient util, String imgsrc, Map<String, String> postdata, int trytimes) {
		String status = "";
		byte[] data = util.GetImg(imgsrc);
		// String codestr="{\"Result\":\"6888\",\"Id\":\"ca3c76d9-88e3-4b4d-9849-ae6c13ad133b\"}";
		String codestr = RuoKuai.createByByte("1040", data);//

		Map<String, Object> map = EasyHttpClient.json2Map(codestr);
		if (map != null) {
			String code = map.get("Result") + "";

			postdata.put("code", code);

			String s = util.Post("http://www.91wangcai.com/user/login", null, postdata);
			Map<String, Object> loginmap = EasyHttpClient.json2Map(s);
			if (loginmap != null) {
				status = loginmap.get("status") + "";
				if ("1.0".equals(status)) {// {"message":"验证码错误或已超时，请点击刷新","status":1,"data":""}
					String message = loginmap.get("message") + "";
					if (message.contains("验证码错误")) {
						String shangbao = RuoKuai.report(map.get("Id") + "");
						System.out.println(shangbao);
						if (trytimes > 0) {
							status = TryIt(util, "http://www.91wangcai.com/common/verifycode?t=" + Math.random(), postdata, --trytimes);
						} else {
							status = "验证码错误";
						}
					} else if (message.contains("帐号或密码有误")) {// {"message":"您输入的帐号或密码有误，请核实","status":"1","data":""}
						status = "帐号或密码有误";
					} else {
						status = message;
					}
				} else if ("0".equals(status)) {
					return status;
				}
			}
		}

		return status;
	}

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public EasyHttpClient Login(String name, String password) {
		EasyHttpClient util = new EasyHttpClient();
		boolean status = Login(util, name, password);
		if (status) {
			return util;
		} else {
			return null;
		}
	}

	/**
	 * 通过用户名密码登陆
	 * 
	 * @param name
	 * @param password
	 * @param util
	 * @return
	 */
	public boolean Login(EasyHttpClient util, String name, String password) {

		// 通用MD5加密即可，加密后转换成大写
		String a = MD5Util.md5(password);

		String loginhtml = util.Get("http://www.91wangcai.com/user/to_login");
		Document doc = Jsoup.parse(loginhtml);
		String imgsrc = doc.select("img#valicodeImg").attr("src");
		imgsrc = EasyHttpClient.tidyUrl("http://www.91wangcai.com/user/to_login", imgsrc);
		Map<String, String> postdata = new LinkedHashMap<String, String>();
		postdata.put("username", name);// {username:username,password:$.md5(password),code:code,csrf:csrfToken},
		postdata.put("password", a);
		postdata.put("csrf", "null");

		String status = TryIt(util, imgsrc, postdata, 3);
		log.info("user " + name + " login status : " + status);
		if (null != status && "0".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 如果想运行本方法，需要到若快申请账号<br>
	 * 并将账号填到RuoKuai.java类里的username和password中<br>
	 * 并且注意图片类型，1040代表4位纯数字，2040代表4位纯字母，3040代表4位数字字母混合
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 自己的账号，口令
		String name = "";
		String password = "";
		LoginUtilInterface Loginutil = new _91WangCaiLoginUtil();
		EasyHttpClient util = new EasyHttpClient();
		Loginutil.Login(util, name, password);
		util.close();
	}
}
