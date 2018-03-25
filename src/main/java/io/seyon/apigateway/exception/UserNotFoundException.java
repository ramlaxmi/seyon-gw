package io.seyon.apigateway.exception;

public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4552855815510172243L;
	
	public UserNotFoundException() {
		super();
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}

}
