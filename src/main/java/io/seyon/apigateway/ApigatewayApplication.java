package io.seyon.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.filters.RoutingFilter;

@SpringBootApplication
@EnableZuulProxy
@EnableConfigurationProperties
@EnableCaching
public class ApigatewayApplication {

	private static Logger log = LoggerFactory.getLogger(ApigatewayApplication.class);

	public static void main(String[] args) {
		log.info("API gateway Initilizing");
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

	@AutoConfigurationPackage
	protected static class AuthServer implements WebMvcConfigurer {

		private static final Logger log = LoggerFactory.getLogger(AuthServer.class);

		@Autowired
		private Environment environment;

		@Autowired
		private SeyonGwProperties properties;

		@Autowired
		HandlerInterceptor authorizationInterceptor;

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(authorizationInterceptor)
				.excludePathPatterns(properties.getAuthExcludeUrl());
		}

	}
}
