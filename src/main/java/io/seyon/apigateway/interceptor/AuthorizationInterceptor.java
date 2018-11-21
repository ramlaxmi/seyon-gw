package io.seyon.apigateway.interceptor;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.seyon.apigateway.common.Constants;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

	private static Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("Url Access {}", request.getRequestURI());
		log.info("Pre handle method - check handling start time");
		long startTime = System.currentTimeMillis();
		String userEmail = null;
		String userSessionId = null;
		OAuth2Authentication principal = (OAuth2Authentication) request.getUserPrincipal();
		request.setAttribute("executionTime", startTime);
		log.info("Principal " + principal);
		try {
			if (principal != null) {
				Authentication authentication = principal.getUserAuthentication();
				Map<String, String> detailsMap = new LinkedHashMap<>();
				detailsMap = (Map<String, String>) authentication.getDetails();
				userEmail = detailsMap.get("email");
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) principal.getDetails();
				userSessionId = details.getSessionId();
			}
		} catch (Exception e) {
			log.error("dumping principal " + principal + "failed, exception: ", e);
		}
		request.setAttribute(Constants.USER_SESSION, userSessionId);
		request.setAttribute(Constants.USER_EMAIL, userEmail);
		return true;	
	}
}
