package com.mbv.framework.token;


public class TokenServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2392067062169207466L;

	private TokenErrorCode errorCode;
	
	public TokenServiceException(Throwable t,TokenErrorCode errorCode) {
		super(t);
		this.errorCode = errorCode;
	}

	public TokenServiceException(TokenErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public TokenErrorCode getErrorCode() {
      return errorCode;
  }

	public enum TokenErrorCode {
		SIGNING_ERROR, INVALID_TOKEN, EXPIRED_TOKEN
	}

	
}
