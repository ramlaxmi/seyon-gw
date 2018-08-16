package io.seyon.apigateway.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

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
import io.seyon.apigateway.entity.UserSession;
import io.seyon.apigateway.exception.InvalidPasswordException;
import io.seyon.apigateway.exception.UserInActiveException;
import io.seyon.apigateway.exception.UserNotFoundException;
import io.seyon.apigateway.model.Success;
import io.seyon.apigateway.repository.UserSessionRepository;

@Service
public class LoginService {

	@Autowired
	@Qualifier("bcryptEncoder")
	PasswordEncoder encoder;


	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	UserSessionRepository userSessRepo;
	
	@Autowired
	SeyonGwProperties properties;

	public boolean authenticate(String email, String password)
			throws UserNotFoundException, UserInActiveException, InvalidPasswordException {
		
		User user = findUserByEmail(email);
		
		if (null == user) {
			throw new UserNotFoundException("user not found");
		}
		if (!user.getActive()) {
			throw new UserInActiveException("User Inactive");
		}
		String encodedPassword = user.getPassword();
		boolean matched = encoder.matches(password, encodedPassword);
		if (!matched) {
			throw new InvalidPasswordException("Password Missmatch");
		}
		return true;

	}
	
	public String getSession(String email,String ip) {
		UUID uuid= UUID.randomUUID();
		String uniqueId=uuid.toString().replaceAll("-", "");
		UserSession us= new UserSession();
		us.setCreatedTime(new Date());
		us.setEmail(email);
		us.setMachineIp(ip);
		us.setSessionId(uniqueId);
		us.setExpiry(properties.getCookieMaxAgeInSeconds());
		Calendar cal= Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND,properties.getCookieMaxAgeInSeconds());
		us.setExpiryTime(cal.getTime());
		userSessRepo.save(us);
		return uniqueId;
	}

	@Transactional
	public void deleteSession(String sessionId) {
		userSessRepo.deleteBySessionId(sessionId);
		return;
		
	}
	
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
