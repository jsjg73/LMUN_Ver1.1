package com.mycompany.myapp.advice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.model.FriendPush;
import com.mycompany.myapp.service.FriendService;

public class pushTestAdvice {
	@Autowired
	FriendService friendService;
	
	public pushTestAdvice() {		
		System.out.println("push obj create");
	}
	public void pushSession(JoinPoint jp) {
		HttpSession session = (HttpSession)jp.getArgs()[0];
		if(session.getAttribute("email")==null)return;
		
		String email =(String)session.getAttribute("email");
		List<FriendPush> invitedList = friendService.invited(email);
        session.setAttribute("fr_push",invitedList);
	}
}
