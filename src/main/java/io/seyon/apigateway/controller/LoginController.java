package io.seyon.apigateway.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.seyon.apigateway.ApigatewayApplication;
import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.exception.InvalidPasswordException;
import io.seyon.apigateway.exception.UserInActiveException;
import io.seyon.apigateway.exception.UserNotFoundException;
import io.seyon.apigateway.service.LoginService;

@Controller
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;
	
	@Autowired
	SeyonGwProperties properties;
	

	@GetMapping("/login")
	public String login(@ModelAttribute User user, Model model) {
		return "login";
	}

	@PostMapping("/login")
	public ModelAndView authenticate(@ModelAttribute User user, Model model,HttpServletResponse response,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		log.info("Loggin in user {}", user);
		
		try {
			loginService.authenticate(user.getEmail(), user.getPassword());
		} catch (UserNotFoundException | UserInActiveException | InvalidPasswordException e) {
			log.error("Error while authenticating", e);

			mav.addObject("exception", e.getMessage());
			mav.addObject("error", true);
			mav.setViewName("login");
			return mav;
		}
		if(StringUtils.isNotBlank(properties.getLoginSuccessUrl())) {
			mav.addObject("redirect", true);
			mav.addObject("redirectUrl",properties.getLoginSuccessUrl());
		}
		String  remoteAddr = request.getHeader("X-FORWARDED-FOR"); 
		String sessionId=loginService.getSession(user.getEmail(),remoteAddr);
		Cookie cookie=new Cookie(Constants.X_AUTH_COOKIE, sessionId);
		cookie.setPath("/");
		cookie.setMaxAge(properties.getCookieMaxAgeInSeconds());
		response.addCookie(cookie);
		mav.setViewName("success");
		return mav;
	}

	/*
	 * @GetMapping("/createUser") public String createUser( Model model) { User user
	 * = new User(); user.setActive(true); user.setEmail("admin@seyon.com");
	 * user.setPassword("seyon"); user.setName("Seyon");
	 * loginService.createUser(user); return "success"; }
	 */

}