package io.seyon.apigateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.service.LoginService;

//@Controller
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;

	@Autowired
	SeyonGwProperties properties;
/*
	@GetMapping("/login")
	public String login(@ModelAttribute User user, Model model, HttpServletRequest request) {
		
		String redirectUri = (String) request.getSession().getAttribute(Constants.REDIRECT_URI);
		user.setRedirectUri(redirectUri);
		String token = TokenGenerator.generateToken("LT");
		request.getSession().setAttribute("LT", token);
		user.setLtToken(token);
		log.info("URL to redirect {}", redirectUri);
		return "login";
	}

	@PostMapping("/login")
	public ModelAndView authenticate(@ModelAttribute User user, Model model, HttpServletResponse response,
			HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		String token = (String) request.getSession().getAttribute("LT");
		if (!token.equals(user.getLtToken())) {
			log.error("Invalid LT token ");
			mav.addObject("exception", "Invalid access token");
			mav.addObject("error", true);
			mav.setViewName("login");
			return mav;
		}

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
		if (StringUtils.isNotBlank(user.getRedirectUri()) && !"/login".equals(user.getRedirectUri()) && !"/".equals(user.getRedirectUri())) {
			mav.addObject("redirect", true);
			mav.addObject("redirectUrl", user.getRedirectUri());

		} else if (StringUtils.isNotBlank(properties.getLoginSuccessUrl())) {
			mav.addObject("redirect", true);
			mav.addObject("redirectUrl", properties.getLoginSuccessUrl());
		}
		String remoteAddr = request.getHeader("X-FORWARDED-FOR");
		String sessionId = loginService.getSession(user.getEmail(), remoteAddr);
		Cookie cookie = new Cookie(Constants.X_AUTH_COOKIE, sessionId);
		cookie.setPath("/");
		cookie.setMaxAge(properties.getCookieMaxAgeInSeconds());
		response.addCookie(cookie);
		mav.setViewName("success");
		return mav;
	}

	@GetMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Cookie[] cookies = request.getCookies();
		String sessionId = null;
		if (null == cookies) {
			log.error("Cookie Not found");
			response.sendRedirect("/login");
			return;
		}
		Cookie cook = null;
		for (Cookie cookie : cookies) {
			log.debug("verifying cookie :{}, path:{}", cookie.getName(), cookie.getPath());
			if (Constants.X_AUTH_COOKIE.equalsIgnoreCase(cookie.getName())) {
				sessionId = cookie.getValue();
				cook = cookie;
				break;
			}
		}
		if (StringUtils.isBlank(sessionId)) {
			log.error("Session id is null");
			response.sendRedirect("/login");
			return;
		}

		log.info("Session id is {}", sessionId);
		loginService.deleteSession(sessionId);
		cook.setMaxAge(0);
		cook.setPath("/");
		response.addCookie(cook);
		response.sendRedirect("/login");
		return;
	}*/
	/*
	 * @GetMapping("/createUser") public String createUser( Model model) { User user
	 * = new User(); user.setActive(true); user.setEmail("admin@seyon.com");
	 * user.setPassword("seyon"); user.setName("Seyon");
	 * loginService.createUser(user); return "success"; }
	 */

}