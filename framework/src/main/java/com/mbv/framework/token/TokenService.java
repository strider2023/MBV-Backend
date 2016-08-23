package com.mbv.framework.token;


public interface TokenService<T> {

	public AccessToken<T> generateToken(T userId, int salt) throws TokenServiceException;

	public boolean validateToken(String token, int salt, long duration) throws TokenServiceException;

}
