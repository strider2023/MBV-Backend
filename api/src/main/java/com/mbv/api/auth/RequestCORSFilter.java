package com.mbv.api.auth;

import com.mbv.api.props.CorsProps;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class RequestCORSFilter extends OncePerRequestFilter {

    @Autowired
    private CorsProps corsProps;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Set<String> allowedOrigins = corsProps.getAllowedOrigins();

        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            String originHeader = request.getHeader("Origin");

            if (StringUtils.isNotBlank(originHeader)) {
                for (String allowedOrigin : allowedOrigins) {
                    if (originHeader.indexOf(allowedOrigin) != -1) {
                        response.addHeader("Access-Control-Allow-Origin", originHeader);
                        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-AUTH-TOKEN");
                        response.addHeader("Access-Control-Max-Age", "1800");
                        response.getWriter().print("OK");
                        response.getWriter().flush();
                        break;
                    }
                }
            }
            return;
        }

        filterChain.doFilter(request, response);
    }
}