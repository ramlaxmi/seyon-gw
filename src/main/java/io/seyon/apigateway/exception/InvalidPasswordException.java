package io.seyon.apigateway.exception;

public class InvalidPasswordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4552855815510172243L;
	
	public InvalidPasswordException() {
		super();
	}
	
	public InvalidPasswordException(String message) {
		super(message);
	}

}
