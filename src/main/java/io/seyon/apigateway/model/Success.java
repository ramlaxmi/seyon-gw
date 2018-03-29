package io.seyon.apigateway.model;

public class Success {

	private Integer code;
	private String message;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Success() {
		
	}
	
	public Success(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

}
