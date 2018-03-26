package io.seyon.apigateway.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import io.seyon.apigateway.entity.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long>{

	public List<UserRole> findByEmail(String email);
}
