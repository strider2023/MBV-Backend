package com.mbv.framework.exception;



public class AppRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5749132182915984790L;

	private ErrorCode errorCode;
	
	public AppRuntimeException(ErrorCode errorCode){
		super();
		this.errorCode = errorCode;
	}
	
	public AppRuntimeException(ErrorCode errorCode, Throwable e){
		super(e);
		this.errorCode = errorCode;
	}
	
	public AppRuntimeException(ErrorCode errorCode, String message){
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}

