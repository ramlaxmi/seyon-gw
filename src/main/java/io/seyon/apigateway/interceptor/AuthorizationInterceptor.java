package io.seyon.apigateway.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.entity.UserSession;
import io.seyon.apigateway.repository.UserSessionRepository;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter  {

	private static Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);
	
	@Autowired
	UserSessionRepository userSessRepo;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)	throws Exception {
		log.info("Url Access {}",request.getRequestURI());
		
		request.getSession().setAttribute("redirectUri", request.getRequestURI());
		
		Cookie[] cookies=request.getCookies();
		String sessionId=null;
		if(null==cookies) {
			log.error("Cookie Not found");
			response.sendRedirect("/login");
			return false;	
		}
		for(Cookie cookie:cookies){
			log.debug("verifying cookie :{}, path:{}",cookie.getName(),cookie.getPath());
			if(Constants.X_AUTH_COOKIE.equalsIgnoreCase(cookie.getName())){
				sessionId=cookie.getValue();
				break;
			}
		}
		if(StringUtils.isBlank(sessionId)){
			log.error("Session id is null");
			response.sendRedirect("/login");
			return false;
		}

		log.info("Session id is {}",sessionId);
		UserSession us=userSessRepo.findBySessionId(sessionId);
		if(null==us || us.getExpiryTime().before(new Date())) {
			log.error("Session Expired");
			response.sendRedirect("/login");
			return false;	
		}
		log.info("User Session details is {}",us);
		return true;
	}
}
