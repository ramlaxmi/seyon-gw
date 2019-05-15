package io.seyon.apigateway.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.model.CompanyModel;
import io.seyon.apigateway.model.CompanyRole;
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
	
	
	@CacheEvict(value= {"/UserByEmail","/UserRolesByUserEmail"},key="#company.userInfo.email")
	public Success createCompany(CompanyModel company) {
		
		company.getCompany().setOwnerName(company.getUserInfo().getName());
		company.getCompany().setPrimaryEmail(company.getUserInfo().getEmail());
		String createCompanyUrl=gwProperties.getRestUrlDomain()+gwProperties.getRestUrlMap().get("createCompany");
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON); 
	    HttpEntity<CompanyModel> entity = new HttpEntity<CompanyModel>(company, headers); 
	    ResponseEntity<Success> response = restTemplate.exchange(createCompanyUrl, HttpMethod.POST, entity, Success.class);
		return response.getBody();
	}
	
	public List<CompanyRole> getCompaniesAndRoleForUser(String email) {
		String url = gwProperties.getRestUrlDomain() + gwProperties.getRestUrlMap().get("getCompanyForUser");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String token=DigestUtils.sha256Hex(gwProperties.getAppId());
		headers.add("app_token",token);
		
		HttpEntity<String> entity = new HttpEntity<String>(email, headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("email", email);
		ResponseEntity<List> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, entity,List.class);
		List<CompanyRole> companyRoles =response.getBody();
		return companyRoles;
	}
}
