package com.bqs.easy.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bqs.easy.spider.core.TaskManager;
import com.bqs.easy.spider.entity.Task;

@Controller
@RequestMapping("/spider.do")
public class SpiderController {

	@RequestMapping(params = "method=add")
	public String update(@ModelAttribute Task t) {
		if (t != null&&!"".equals(t.getMainURL())) {
			TaskManager.getInstance().addTask(t);
		} else {
			System.out.println(null + "");
		}
		return "success";
	}

}
