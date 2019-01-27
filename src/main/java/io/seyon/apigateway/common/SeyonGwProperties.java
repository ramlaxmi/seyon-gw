package io.seyon.apigateway.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "seyon.gw")
public class SeyonGwProperties {

	List<String> authExcludeUrl = new ArrayList<>();
	String loginSuccessUrl;
	Integer cookieMaxAgeInSeconds;
	Map<String, String> restUrlMap;
	String restUrlDomain;

	String proxyHost;
	Integer proxyPort;
	String proxyUser;
	String proxyPassword;
	Boolean useProxyFlag;
	Boolean sslValidationFlag;

	String appId;
	
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

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public Boolean isUseProxyFlag() {
		return useProxyFlag;
	}

	public void setUseProxyFlag(Boolean useProxyFlag) {
		this.useProxyFlag = useProxyFlag;
	}

	public Boolean isSslValidationFlag() {
		return sslValidationFlag;
	}

	public void setSslValidationFlag(Boolean sslValidationFlag) {
		this.sslValidationFlag = sslValidationFlag;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}



}
