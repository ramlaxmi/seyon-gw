package io.seyon.apigateway.repository;

import org.springframework.data.repository.CrudRepository;

import io.seyon.apigateway.entity.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession, String>{

	public UserSession findBySessionId(String sessionId);
	
}
