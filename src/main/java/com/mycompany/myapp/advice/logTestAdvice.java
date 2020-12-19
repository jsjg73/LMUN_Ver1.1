package com.mycompany.myapp.advice;

import org.springframework.stereotype.Component;

public class logTestAdvice {
	public logTestAdvice() {		
		System.out.println("advice obj create");
	}
	public void printLog() {
		System.out.println("=======================================");
		System.out.println("aop test");
		System.out.println("=======================================");
	}
}
