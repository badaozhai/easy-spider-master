package com.bqs.easy.spider.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.bqs.easy.spider.util.MyStringUtil;

/**
 *
 * @author xym
 */

public class SystemFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(SystemFilter.class);

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
			ServletException {
		if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
			throw new ServletException("OncePerRequestFilter just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		HttpSession session = httpRequest.getSession(true);

		Object object = session.getAttribute("islogin");
		if (MyStringUtil.readBoolean(object)) {
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "您未登录或者您已经太长时间没有操作,请刷新页面");
			httpResponse.sendRedirect("/login.html");
			return;
		}
		filterChain.doFilter(servletRequest, servletResponse);
		return;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
