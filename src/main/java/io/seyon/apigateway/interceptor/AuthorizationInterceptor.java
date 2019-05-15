package io.seyon.apigateway.interceptor;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.service.LoginService;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

	private static Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);

	@Autowired
	ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("Url Access {}", request.getRequestURI());
		long startTime = System.currentTimeMillis();
		String userEmail = null;
		String userSessionId = null;
		OAuth2Authentication principal = (OAuth2Authentication) request.getUserPrincipal();
		request.setAttribute("executionTime", startTime);
		log.debug("Principal " + principal);
		User user = null;
		try {
			if (principal != null) {
				Authentication authentication = principal.getUserAuthentication();
				Map<String, String> detailsMap = new LinkedHashMap<>();
				detailsMap = (Map<String, String>) authentication.getDetails();
				userEmail = detailsMap.get("email");
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) principal.getDetails();
				userSessionId = details.getSessionId();
				LoginService loginService= context.getBean(LoginService.class);
				user = loginService.findUserByEmail(userEmail);
				
				Cookie[] cookies= request.getCookies();
				String companyCookieValue=null;
				for(Cookie c:cookies) {
					if(c.getName().equalsIgnoreCase(Constants.USER_PREFERENCE_COOKIE)) {
						companyCookieValue=c.getValue();
					}
				}
				if(null==user) {
					response.sendRedirect("/userNotFound");
					return false;
				}
				/*
				 * if(null==companyCookieValue) { response.sendRedirect("/"); return true; }
				 */
				 				
			}
		} catch (Exception e) {
			log.error("Restricting principal " + principal + "failed, exception: {} ", e);
			return false;
		}
		request.setAttribute(Constants.USER_SESSION, userSessionId);
		request.setAttribute(Constants.USER, user);
		return true;	
	}
}
