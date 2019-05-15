package io.seyon.apigateway.model;

import java.util.List;

public class CompanyRole {

	Long companyId;
	
	String companyName;
	
	List<String> roleCode;

	
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<String> getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(List<String> roleCode) {
		this.roleCode = roleCode;
	}
	
}
