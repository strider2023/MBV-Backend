package com.mbv.framework.exception;



public interface ErrorCode {
	
	public static ErrorCode UNEXPECTED_ERROR = new ErrorCode(){
		@Override
		public int getResponseCode() {
			return 500;
		}

		@Override
		public String getDefaultMessage() {
			return "UNEXPECTED_ERROR";
		}
		
	};
	
	public int getResponseCode();
	
	public String getDefaultMessage();
	
}
