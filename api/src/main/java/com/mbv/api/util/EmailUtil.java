package com.mbv.api.util;

import java.util.regex.Pattern;

public class EmailUtil {

	private static Pattern pattern;
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	static{
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	public static boolean isEmailValid(String email){
		return pattern.matcher(email).matches();
	}
}
