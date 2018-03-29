package io.seyon.apigateway.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="seyon.gw")
public class SeyonGwProperties {

	List<String> authExcludeUrl= new ArrayList<>();
	String loginSuccessUrl;
	Integer cookieMaxAgeInSeconds;
	Map<String, String> restUrlMap;
	String restUrlDomain;
	
	public String getLoginSuccessUrl() {
		return loginSuccessUrl;
	}

	public void setLoginSuccessUrl(String loginSuccessUrl) {
		this.loginSuccessUrl = loginSuccessUrl;
	}

	public List<String> getAuthExcludeUrl() {
		return authExcludeUrl;
	}

	public void setAuthExcludeUrl(List<String> authExcludeUrl) {
		this.authExcludeUrl = authExcludeUrl;
	}

	public Integer getCookieMaxAgeInSeconds() {
		return cookieMaxAgeInSeconds;
	}

	public void setCookieMaxAgeInSeconds(Integer cookieMaxAgeInSeconds) {
		this.cookieMaxAgeInSeconds = cookieMaxAgeInSeconds;
	}

	public Map<String, String> getRestUrlMap() {
		return restUrlMap;
	}

	public void setRestUrlMap(Map<String, String> restUrlMap) {
		this.restUrlMap = restUrlMap;
	}

	public String getRestUrlDomain() {
		return restUrlDomain;
	}

	public void setRestUrlDomain(String restUrlDomain) {
		this.restUrlDomain = restUrlDomain;
	}
	
}
