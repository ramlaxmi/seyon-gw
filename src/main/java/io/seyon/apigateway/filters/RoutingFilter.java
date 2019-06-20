package io.seyon.apigateway.filters;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.entity.UserRole;
import io.seyon.apigateway.service.LoginService;

public class RoutingFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(RoutingFilter.class);

	@Autowired
	LoginService loginService;
	
	@Autowired
	SeyonGwProperties props;

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_TOKEN_TYPE = "Bearer";

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		HttpServletResponse response = ctx.getResponse();

		User user = (User) request.getAttribute(Constants.USER);
		String sessionId = (String) request.getAttribute(Constants.USER_SESSION);
		if (StringUtils.isNotBlank(sessionId)) {

			ctx.addZuulRequestHeader(Constants.USER_EMAIL_HEADER, user.getEmail());
			ctx.addZuulRequestHeader(Constants.USER_SESSION_ID_HEADER, sessionId);
			ctx.addZuulRequestHeader(Constants.USER_NAME_HEADER, user.getName());
		
			if(!request.getRequestURI().equals("/admin")) {
				if (null != user.getCompanyId())
					ctx.addZuulRequestHeader(Constants.COMPANY_ID, user.getCompanyId().toString());
				else {
					log.error("Company is not configured for this user");
					try {
						response.sendError(422, "Company is not configured for this user");
					} catch (IOException e) {
						log.error("Error send response", e);
					}
				}
			}else if(request.getRequestURI().equals("/admin") || request.getRequestURI().equals("/su")) {
				//verify whether user is super user or not
				if(null==user.getSuperUser() || !user.getSuperUser()) {
					log.error("You are not super user");
					try {
						response.sendError(422, "Company is not configured for this user");
					} catch (IOException e) {
						log.error("Error send response", e);
					}
				}
			}
			String token=DigestUtils.sha256Hex(props.getAppId());
			ctx.addZuulRequestHeader("app_token",token);
		}

		log.debug(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		
		return null;
	}

	@Override
	public boolean shouldFilter() {	
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}
