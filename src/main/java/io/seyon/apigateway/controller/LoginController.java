package io.seyon.apigateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.service.LoginService;

public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;

	@Autowired
	SeyonGwProperties properties;

	/*
	 * @GetMapping("/createUser") public String createUser( Model model) { User user
	 * = new User(); user.setActive(true); user.setEmail("admin@seyon.com");
	 * user.setPassword("seyon"); user.setName("Seyon");
	 * loginService.createUser(user); return "success"; }
	 */

}