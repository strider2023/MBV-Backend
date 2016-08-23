package com.mbv.api.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.mbv.api.constant.APIConstants;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.cache.CacheClient;
import com.mbv.framework.util.HTTPUtil;
import com.mbv.framework.util.JSONUtil;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.entity.User;
import com.mbv.persist.enums.Status;
import com.sun.jersey.api.core.InjectParam;

public class TokenAuthenticationFilter extends GenericFilterBean {

	private CacheClient cacheClient;

    private UserDAO userDAO;
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if( SecurityContextHolder.getContext().getAuthentication() == null){
			UserContext authentication = builtAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}

	public void setCacheClient(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private String getRemoteIP(HttpServletRequest httpReq){
		String remoteIP = httpReq.getHeader("X-FORWARDED-FOR");
		if(!StringUtil.isNullOrEmpty(remoteIP)){
			return remoteIP;
		}
		return httpReq.getRemoteAddr(); 
	}
	
	private UserContext builtAuthentication(ServletRequest request){
		HttpServletRequest httpReq = (HttpServletRequest)request;
		
		//Load from Query parameter
		String authToken = HTTPUtil.findQueryParam(httpReq, APIConstants.AUTH_TOKEN_KEY);
		//else, load from header
		if(authToken == null){
			authToken = httpReq.getHeader(APIConstants.AUTH_TOKEN_HEADER);
		}
		//else, load from cookie
		if(authToken == null){
			authToken = HTTPUtil.findCookie(httpReq,APIConstants.AUTH_TOKEN_COOKIE);
		}
	
		if(authToken != null){
			String token = (String)cacheClient.get(APIConstants.AUTH_TOKEN_KEY + authToken);
			if(token != null){
				UserContext uc =  JSONUtil.getObject(token, UserContext.class);
                User user = userDAO.get(uc.getUserId());
                if(user == null) {
                    throw new AccessDeniedException("INVALID_USER");
                } else if(user.getStatus() != Status.ACTIVE) {
                    throw new AccessDeniedException("INVALID_USER");
                }
                //Disable ip checks for now
				/*if(!getRemoteIP(httpReq).equals(uc.getIp())){
					throw new AccessDeniedException("INVALID_IP");
				}*/
				if(uc.isExpired(APIConstants.SESSION_VALIDITY)){
					throw new AccessDeniedException("AUTH_TOKEN_EXPIRED");
				}
				return uc;
			}
			else{
				throw new AccessDeniedException("INVALID_TOKEN");
			}
		}
		else{
			throw new AccessDeniedException("TOKEN_NOT_FOUND");
		}
	}	

}
