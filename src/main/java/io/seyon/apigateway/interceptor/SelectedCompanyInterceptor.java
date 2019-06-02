package io.seyon.apigateway.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.entity.User;

@Component
public class SelectedCompanyInterceptor extends HandlerInterceptorAdapter {

	private static Logger log = LoggerFactory.getLogger(SelectedCompanyInterceptor.class);

	@Autowired
	ApplicationContext context;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("Url Access {}", request.getRequestURI());
		User user = (User) request.getAttribute(Constants.USER);;
		try {
		Cookie[] cookies= request.getCookies();
		String companyCookieValue=null;
			for(Cookie c:cookies) {
				if(c.getName().equalsIgnoreCase(Constants.USER_PREFERENCE_COOKIE)) {
					companyCookieValue=c.getValue();
				}
			}
		if(!request.getRequestURI().equals("/admin")) {
			if(null==companyCookieValue || companyCookieValue.equals("")) { 
	    	 response.sendRedirect("/"); 
	    	 return false;
	     }
		
	     user.setCompanyId(Long.parseLong(companyCookieValue));
		}
		} catch (Exception e) {
			log.error("exception", e);
			return false;
		}
		request.setAttribute(Constants.USER, user);
		return true;	
	}
}
