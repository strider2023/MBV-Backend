package com.mbv.framework.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class HTTPUtil {

	public static final String findCookie(HttpServletRequest request, String cookieName) {
		String value  = null;
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies){
			if(cookieName.equals(cookie.getName())){
				value = cookie.getValue();
				break;
			}
		}
		return value;
	}

	public static final String findQueryParam(HttpServletRequest request, String paramKey) {
		String queryString = request.getQueryString();
		String token = null;
		if(queryString != null){
			String[] split = queryString.split("&");
			for(String s: split){
				if(s.startsWith(paramKey)){
					String[] param = s.split("=");
					token = param[1];
					break;
				}
			}
		}
		return token;
	}
	
	public static final Map<String,String> parseQueryString(String queryString) {
		Map<String,String> params = new HashMap<String,String>();
		if(queryString != null){
			String[] split = queryString.split("&");
			for(String s: split){
				String[] param = s.split("=");
				params.put(param[0],param[1]);
			}
		}
		return params;
	}
}
