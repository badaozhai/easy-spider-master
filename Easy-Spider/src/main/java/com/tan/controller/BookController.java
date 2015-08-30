package com.tan.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/book.do")
public class BookController {

	@RequestMapping(params = "method=update")
	public String update() {
		return "success";
	}

}
