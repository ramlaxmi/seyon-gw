package io.seyon.apigateway.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.entity.UserRole;

@Service
public class LoginService {

	@Autowired
	@Qualifier("bcryptEncoder")
	PasswordEncoder encoder;


	@Autowired
	private RestTemplate restTemplate;
	
	
	@Autowired
	SeyonGwProperties properties;

		
	@Cacheable("/UserByEmail")
	public User findUserByEmail(String email) {
		String url=properties.getRestUrlDomain()+properties.getRestUrlMap().get("findUserByEmail");
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(url)
				.queryParam("email", email);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON); 
	    HttpEntity<String> entity = new HttpEntity<String>(email, headers); 
	    url=url.concat("?email=").concat(email);
	    ResponseEntity<User> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, entity, User.class);
		return response.getBody();
	}
	
	@Cacheable("/UserRolesByUserEmail")
	public List<UserRole> findRolesByUserEmail(String email){
		//findRolesByUserEmail
		String url=properties.getRestUrlDomain()+properties.getRestUrlMap().get("findRolesByUserEmail");
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON); 
	    HttpEntity<String> entity = new HttpEntity<String>(email, headers); 
	    UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(url)
				.queryParam("email", email);
	    ResponseEntity<UserRole[]> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, entity, UserRole[].class);
	    UserRole roles[]= response.getBody();
		return Arrays.asList(roles);
	}
}
