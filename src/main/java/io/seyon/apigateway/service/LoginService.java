package io.seyon.apigateway.service;

import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.seyon.apigateway.common.SeyonGwProperties;
import io.seyon.apigateway.entity.User;
import io.seyon.apigateway.entity.UserSession;
import io.seyon.apigateway.exception.InvalidPasswordException;
import io.seyon.apigateway.exception.UserInActiveException;
import io.seyon.apigateway.exception.UserNotFoundException;
import io.seyon.apigateway.repository.UserRepository;
import io.seyon.apigateway.repository.UserSessionRepository;

@Service
public class LoginService {

	@Autowired
	@Qualifier("bcryptEncoder")
	PasswordEncoder encoder;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserSessionRepository userSessRepo;
	
	@Autowired
	SeyonGwProperties properties;

	public boolean authenticate(String email, String password)
			throws UserNotFoundException, UserInActiveException, InvalidPasswordException {
		User user = userRepo.findByEmail(email);
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

	public User createUser(User user) {
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		return userRepo.save(user);
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
		userSessRepo.save(us);
		return uniqueId;
	}

	@Transactional
	public void deleteSession(String sessionId) {
		userSessRepo.deleteBySessionId(sessionId);
		return;
		
	}
}
