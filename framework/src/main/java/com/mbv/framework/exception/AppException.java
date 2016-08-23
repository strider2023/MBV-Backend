package com.mbv.framework.exception;

import java.util.Set;

public class AppException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4819026441523138053L;
	
	private Set<ErrorCode> errorCodes;
	
	public AppException(Set<ErrorCode> errorCodes){
		super();
		this.errorCodes = errorCodes;
	}
	
	public AppException(Set<ErrorCode> errorCodes, Throwable e){
		super(e);
		this.errorCodes = errorCodes;
	}
	
	public AppException(Set<ErrorCode> errorCodes, String message){
		super(message);
		this.errorCodes = errorCodes;
	}

	public Set<ErrorCode> getErrorCodes() {
		return errorCodes;
	}


}
