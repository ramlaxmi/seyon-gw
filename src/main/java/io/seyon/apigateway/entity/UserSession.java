package io.seyon.apigateway.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserSession {

	@Id
	private String sessionId;
	@Column
	private String email;
	
	@Column
	private String machineIp;
	
	@Column
	private Date createdTime;
	
	@Column
	private Integer expiry;
	
	@Column
	private Date expiryTime;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMachineIp() {
		return machineIp;
	}

	public void setMachineIp(String machineIp) {
		this.machineIp = machineIp;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getExpiry() {
		return expiry;
	}

	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}

	@Override
	public String toString() {
		return "UserSession [sessionId=" + sessionId + ", email=" + email + ", machineIp=" + machineIp
				+ ", createdTime=" + createdTime + ", expiry=" + expiry + "]";
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
	
	
}
