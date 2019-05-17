package io.seyon.apigateway.entity;

public class UserRole {


	private Long id;
	private String email;
	private String roleCode;
	private Long companyId;

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

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
