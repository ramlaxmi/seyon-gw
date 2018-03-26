package io.seyon.apigateway.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import io.seyon.apigateway.entity.User;

public interface UserRepository extends CrudRepository<User, String>{

	User findByEmail(String email);
}
