package io.seyon.apigateway.entity;

public class User {


	String email;
	String name;
	String password;
	Boolean active;
	Long companyId;
	String redirectUri;
	String ltToken;
	
	Boolean superUser;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getLtToken() {
		return ltToken;
	}

	public void setLtToken(String ltToken) {
		this.ltToken = ltToken;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", active=" + active + ", companyId=" + companyId + "]";
	}

	public Boolean getSuperUser() {
		return superUser;
	}

	public void setSuperUser(Boolean superUser) {
		this.superUser = superUser;
	}
}
