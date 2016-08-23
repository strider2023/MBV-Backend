package com.mbv.api.auth;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GenericAuthenticationFilter extends GenericFilterBean implements InitializingBean {

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String header = ((HttpServletRequest) request).getHeader("Authorization");
            if (header == null || !header.startsWith("Basic ")) {
                throw new BadCredentialsException("Invalid basic authentication token");
            }
            String[] tokens = extractAndDecodeHeader(header, (HttpServletRequest) request);
            if (!(tokens[0].equals("API") && tokens[1].equals("API_USER"))) {
                throw new BadCredentialsException("Invalid basic authentication token");
            }
            SecurityContextHolder.getContext().setAuthentication(createAuthentication((HttpServletRequest) request));
        }
        chain.doFilter(request, response);
    }

    protected Authentication createAuthentication(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("API", "API_USER", AuthorityUtils.createAuthorityList("API_USER"));
        auth.setDetails(authenticationDetailsSource.buildDetails(request));
        return auth;
    }

    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] {token.substring(0, delim), token.substring(delim + 1)};
    }

}
