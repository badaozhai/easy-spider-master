package com.bqs.easy.spider.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bqs.easy.spider.entity.User;

@Controller
public class IndexController {

	// @RequestMapping(params = "/index.html")
	@RequestMapping({ "/index.html" })
	public String index(@RequestHeader("User-Agent") String userAgent, @RequestHeader("cookie") String cookie) {
		if (userAgent.contains("Mobile")) {
			return "mindex";
		} else {
			return "index";
		}
	}

	@RequestMapping({ "/login.html" })
	public String loginpage(@ModelAttribute User u, HttpServletRequest request) {
		return "login";
	}

	@RequestMapping({ "/login" })
	public String login(@ModelAttribute User u, HttpServletRequest request) {
		if (u != null && !"".equals(u.getUsername())) {
			System.out.println(u);
			request.getSession().setAttribute("islogin", "true");
		} else {
			System.out.println(null + "");
		}
		return "index";
	}

}
