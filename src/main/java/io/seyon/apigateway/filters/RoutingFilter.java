package io.seyon.apigateway.filters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.seyon.apigateway.common.Constants;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.entity.UserRole;
import io.seyon.apigateway.repository.UserRepository;
import io.seyon.apigateway.repository.UserRoleRepository;

public class RoutingFilter extends ZuulFilter{

	private static Logger log = LoggerFactory.getLogger(RoutingFilter.class);
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserRoleRepository userRoleRepo;
	
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();
	    
	    
	    String email=(String) request.getAttribute(Constants.USER_EMAIL);
	    String sessionId=(String) request.getAttribute(Constants.USER_SESSION);
	    User user=userRepo.findByEmail(email);
	    List<UserRole> userRoles= userRoleRepo.findByEmail(email);
	    
	    ctx.addZuulRequestHeader("USER-ID",null);
	    
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
