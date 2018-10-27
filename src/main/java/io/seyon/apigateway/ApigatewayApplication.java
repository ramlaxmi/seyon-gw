package io.seyon.apigateway;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.filters.OauthFilter;
import io.seyon.apigateway.filters.RoutingFilter;
import io.seyon.apigateway.service.RequestService;

@SpringBootApplication
@EnableZuulProxy
@EnableConfigurationProperties
@EnableCaching
@EnableWebSecurity
public class ApigatewayApplication extends WebSecurityConfigurerAdapter{

	private static Logger log = LoggerFactory.getLogger(ApigatewayApplication.class);

	@Autowired
	private SeyonGwProperties properties;
	
	@Autowired
	private RequestService reqService;
	

	@Autowired
	@Qualifier("oauth2ClientContext")
	OAuth2ClientContext oauth2ClientContext;

	@Autowired
	OAuth2ProtectedResourceDetails resource;
	
	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}

	@Bean
	public RoutingFilter simpleFilter() {
		return new RoutingFilter();
	}

	@Bean
	public PasswordEncoder bcryptEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	@Bean
	public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {   
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}
	

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		List<String> urlList=properties.getAuthExcludeUrl();
		String urls[]=new String[urlList.size()];
		urls=urlList.toArray(urls);
		http
			.antMatcher("/**")
			.authorizeRequests()
			.antMatchers(urls)
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.oauth2Login()
			.defaultSuccessUrl("/ui");
			
	}
}
