package io.seyon.apigateway.repository;

import org.springframework.data.repository.CrudRepository;

import io.seyon.apigateway.entity.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession, String>{

	UserSession findBySessionId(String sessionId);
	
	void deleteBySessionId(String sessionId);
	
}
