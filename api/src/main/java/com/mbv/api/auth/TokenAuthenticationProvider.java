package com.mbv.api.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class TokenAuthenticationProvider implements AuthenticationProvider{

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if(!this.supports(authentication.getClass())){
			return null;
		}
		return authentication;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return UserContext.class.isAssignableFrom(clazz);
	}
	
}
