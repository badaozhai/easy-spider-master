package com.bqs.easy.spider.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bqs.easy.spider.entity.Task;
import com.bqs.easy.spider.manager.TaskManager;

@Controller
@RequestMapping("/spider.do")
public class SpiderController {
	private static Log logger = LogFactory.getLog(TaskManager.class);

	@RequestMapping(params = "method=add")
	public ModelAndView update(@ModelAttribute Task t) {

		ModelAndView v = new ModelAndView();

		if (t != null && !"".equals(t.getMainURL())) {
			String url = t.getMainURL();
			if (url.matches("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?")) {
				TaskManager.getInstance().addTask(t);
				v.setViewName("success");
			} else {
				logger.error("URL不合法");
				v.addObject("fail", "URL不合法");
				v.setViewName("fail");
			}
		} else {
			v.addObject("fail", "任务为空");
			v.setViewName("fail");
		}
		return v;
	}

}
