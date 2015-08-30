package com.bqs.easy.spider.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/spider.do")
public class SpiderController {

	@RequestMapping(params = "method=update")
	public String update() {
		return "success";
	}

}
