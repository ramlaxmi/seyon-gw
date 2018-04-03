package io.seyon.apigateway.controller;

import org.springframework.boot.web.servlet.error.ErrorController;

public class CustomErrorController implements ErrorController {

	private static final String PATH = "/error";
	
	@Override
	public String getErrorPath() {
		return PATH;
	}
	
	

}
