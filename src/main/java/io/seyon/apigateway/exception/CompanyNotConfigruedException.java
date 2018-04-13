package io.seyon.apigateway.exception;

public class CompanyNotConfigruedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7587702226239361814L;

	public CompanyNotConfigruedException() {
		super();
	}
	
	public CompanyNotConfigruedException(String message) {
		super(message);
	}
}
