package io.seyon.apigateway.model;



public class CompanyModel {
	
	private User userInfo;
	
	private Company company;

	private String ltToken;
	
	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getLtToken() {
		return ltToken;
	}

	public void setLtToken(String ltToken) {
		this.ltToken = ltToken;
	}

	@Override
	public String toString() {
		return "CompanyModel [userInfo=" + userInfo + ", company=" + company + ", ltToken=" + ltToken + "]";
	}

}
