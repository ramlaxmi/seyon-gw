package io.seyon.apigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.model.CompanyModel;
import io.seyon.apigateway.model.Success;

@Service
public class UserService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	SeyonGwProperties gwProperties;
	
	
	
	public Success resetPassword(String email) {
		String resetPasswordUrl=gwProperties.getRestUrlDomain()+gwProperties.getRestUrlMap().get("resetpassword");
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON); 
	    HttpEntity<String> entity = new HttpEntity<String>(email, headers); 
	    ResponseEntity<Success> response = restTemplate.exchange(resetPasswordUrl, HttpMethod.PUT, entity, Success.class);
		return response.getBody();
	}
	
	
	public Success createCompany(CompanyModel company) {
		String createCompanyUrl=gwProperties.getRestUrlDomain()+gwProperties.getRestUrlMap().get("createCompany");
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON); 
	    HttpEntity<CompanyModel> entity = new HttpEntity<CompanyModel>(company, headers); 
	    ResponseEntity<Success> response = restTemplate.exchange(createCompanyUrl, HttpMethod.POST, entity, Success.class);
		return response.getBody();
	}
	
}
