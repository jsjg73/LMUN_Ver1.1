package com.mycompany.myapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionCheckInter extends HandlerInterceptorAdapter {
	
	//preHandle(request,response,handler) method
	//preHandle() called before Controller get request 
	//move to sign in page if session is null
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String email = (String)session.getAttribute("email");
		if (email == null || email.equals(""))  {		
			response.sendRedirect("member_session.do");
			return false;
		}
		return true;
	}
}
