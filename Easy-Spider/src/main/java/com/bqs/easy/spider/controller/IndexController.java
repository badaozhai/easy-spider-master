package com.bqs.easy.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bqs.easy.spider.entity.Task;

@Controller
public class IndexController {

	// @RequestMapping(params = "/index.html")
	@RequestMapping({ "/index.html" })
	public String update(@ModelAttribute Task t) {
		return "index";
	}

}
