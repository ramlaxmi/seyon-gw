package io.seyon.apigateway.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class User {


	@Id
	String email;
	
	@Column
	String name;
	
	@Column
	String password;
	
	@Column
	Boolean active;
	
	@Column
	Integer companyId;
	
	@Transient
	String redirectUri;
	
	@Transient
	String ltToken;
	
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}


	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", active=" + active + ", companyId=" + companyId + "]";
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

	
}
