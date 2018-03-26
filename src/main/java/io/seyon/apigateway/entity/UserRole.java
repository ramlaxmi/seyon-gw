package io.seyon.apigateway.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserRole {

	@Id
	private Long id;
	
	@Column
	private String email;
	


	@Column
	private String roleCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	@Override
	public String toString() {
		return "UserRole [id=" + id + ", email=" + email + ", roleCode=" + roleCode + "]";
	}
}
