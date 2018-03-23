package io.seyon.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import io.seyon.apigateway.filters.RoutingFilter;

@SpringBootApplication
@EnableZuulProxy
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
}
