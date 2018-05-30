package io.seyon.apigateway.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.entity.UserRole;
import io.seyon.apigateway.exception.CompanyNotConfigruedException;
import io.seyon.apigateway.repository.UserRepository;
import io.seyon.apigateway.repository.UserRoleRepository;

public class RoutingFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(RoutingFilter.class);

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserRoleRepository userRoleRepo;

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		HttpServletResponse response=ctx.getResponse();
		
		String email = (String) request.getAttribute(Constants.USER_EMAIL);
		String sessionId = (String) request.getAttribute(Constants.USER_SESSION);
		if (StringUtils.isNotBlank(sessionId)) {
			User user = userRepo.findByEmail(email);
			List<UserRole> userRoles = userRoleRepo.findByEmail(email);
			String roleCodes = userRoles // -> List<A>
					.stream() // -> Stream<A>
					.map(UserRole::getRoleCode) // -> Stream<String>
					.collect(Collectors.joining(","));
			log.debug("Role codes from user role {}",roleCodes);
			ctx.addZuulRequestHeader(Constants.USER_EMAIL_HEADER, user.getEmail());
			ctx.addZuulRequestHeader(Constants.USER_SESSION_ID_HEADER, sessionId);
			ctx.addZuulRequestHeader(Constants.USER_NAME_HEADER, user.getName());
			ctx.addZuulRequestHeader(Constants.USER_ROLE_HEADER, roleCodes);
			if(null!=user.getCompanyId())
				ctx.addZuulRequestHeader(Constants.COMPANY_ID, user.getCompanyId().toString());
			else {
				log.error("Company is not configured for this user");
				try {
					response.sendError(422,"Company is not configured for this user");
				} catch (IOException e) {
					log.error("Error send response",e);
				}
			}
			
		}

		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
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
