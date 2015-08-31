package com.bqs.easy.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.entity.User;

@Controller
public class IndexController {

	// @RequestMapping(params = "/index.html")
	@RequestMapping({ "/index.html" })
	public String index(@ModelAttribute Task t, @RequestHeader("User-Agent") String userAgent, @RequestHeader("cookie") String cookie) {
		if (userAgent.contains("Mobile")) {
			return "mindex";
		} else {
			return "index";
		}
	}

	@RequestMapping({ "/login.html" })
	public String login(@ModelAttribute Task t, @RequestHeader("User-Agent") String userAgent) {
		if (userAgent.contains("Mobile")) {
			return "mlogin";
		} else {
			return "login";
		}
	}
	
	@RequestMapping({ "/login" })
	public String update(@ModelAttribute User u) {
		if (u != null&&!"".equals(u.getUsername())) {
			System.out.println(u);
		} else {
			System.out.println(null + "");
		}
		return "index";
	}

}
