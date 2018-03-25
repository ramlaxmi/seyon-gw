package io.seyon.apigateway.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="seyon.gw")
public class SeyonGwProperties {

	List<String> authExcludeUrl= new ArrayList<>();
	
	String loginSuccessUrl;
	Integer cookieMaxAgeInSeconds;

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
	
}
