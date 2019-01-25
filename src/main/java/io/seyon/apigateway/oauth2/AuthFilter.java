package io.seyon.apigateway.oauth2;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter {
	
	private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);


	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Autowired
	OAuth2ProtectedResourceDetails resource;

	@Autowired
	ResourceServerProperties resourceServer;

	@Autowired
	RequestHelper requestHelper;
	
	@Autowired
	SeyyonOaut2SuccessHandler successHandler;

	public Filter oauthFilter() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	    OAuth2ClientAuthenticationProcessingFilter oauthFilter = new OAuth2ClientAuthenticationProcessingFilter("/login");
	    log.info("Using auth filter");
	    OAuth2RestTemplate oauthTemplate = new OAuth2RestTemplate(resource, oauth2ClientContext);
	    OAuth2AccessTokenSupport authAccessProvider = new AuthorizationCodeAccessTokenProvider();
	    
	    // Set request factory for '/oauth/token'
	    authAccessProvider.setRequestFactory(requestHelper.getRequestFactory());
	    AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(Arrays.<AccessTokenProvider> asList(
	            (AuthorizationCodeAccessTokenProvider)authAccessProvider));
	    oauthTemplate.setAccessTokenProvider(accessTokenProvider);
	    
	    // Set request factory for '/userinfo'
	    oauthTemplate.setRequestFactory(requestHelper.getRequestFactory());
	    oauthFilter.setRestTemplate(oauthTemplate);
	    
	   //  add succcess handler here 
	   oauthFilter.setAuthenticationSuccessHandler(successHandler);
	    
	    UserInfoTokenServices userInfoTokenService = new UserInfoTokenServices(resourceServer.getUserInfoUri(), resource.getClientId());
	    userInfoTokenService.setRestTemplate(oauthTemplate);
	    oauthFilter.setTokenServices(userInfoTokenService);
	    return oauthFilter;
	}
	
	
}
