package io.seyon.apigateway.exception;

public class UserInActiveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4552855815510172243L;
	
	public UserInActiveException() {
		super();
	}
	
	public UserInActiveException(String message) {
		super(message);
	}

}
