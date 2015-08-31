package com.bqs.easy.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bqs.easy.spider.entity.Task;

@Controller
public class IndexController {

	// @RequestMapping(params = "/index.html")
	@RequestMapping({ "/index.html" })
	public String update(@ModelAttribute Task t, @RequestHeader("User-Agent") String userAgent) {
		if (userAgent.contains("Mobile")) {
			return "mindex";
		} else {
			return "index";
		}
	}

}
