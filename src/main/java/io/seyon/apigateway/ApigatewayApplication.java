package io.seyon.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.common.SeyyonAuthSuccessHandler;
import io.seyon.apigateway.filters.RoutingFilter;

@SpringBootApplication
@EnableZuulProxy
@EnableConfigurationProperties
@EnableCaching
@EnableOAuth2Sso
public class ApigatewayApplication extends WebSecurityConfigurerAdapter{

	private static Logger log = LoggerFactory.getLogger(ApigatewayApplication.class);

	@Autowired
	private SeyonGwProperties properties;
	
	@Autowired
	private SeyyonAuthSuccessHandler successHandler;
	
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
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String[] arr=new String[properties.getAuthExcludeUrl().size()];
		http
		.csrf()
		.disable()
		.antMatcher("/**").authorizeRequests()
		.antMatchers(properties.getAuthExcludeUrl().toArray(arr)).permitAll()
		.anyRequest().authenticated().and()
		.logout().logoutSuccessUrl("/logoutSuccess");
	}
	
	@AutoConfigurationPackage
	protected static class AuthServer implements WebMvcConfigurer {

		private static final Logger log = LoggerFactory.getLogger(AuthServer.class);

		@Autowired
		private SeyonGwProperties properties;

		@Autowired
		HandlerInterceptor authorizationInterceptor;

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry
				.addInterceptor(authorizationInterceptor)
				.excludePathPatterns(properties.getAuthExcludeUrl());
		}

	}
}
