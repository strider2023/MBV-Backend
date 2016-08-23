package com.mbv.api.auth;

import com.mbv.api.constant.APIConstants;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

public class GenericRequestFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try{
			MDC.put(APIConstants.REQUEST_ID, java.util.UUID.randomUUID().toString());
			chain.doFilter(request, response);
		}
		finally{
			MDC.clear();
		}
	}

}
