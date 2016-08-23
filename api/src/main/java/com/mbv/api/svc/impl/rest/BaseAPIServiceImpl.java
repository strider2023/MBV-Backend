package com.mbv.api.svc.impl.rest;

import com.mbv.api.auth.UserContext;
import com.mbv.api.data.BaseResponse;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.cache.CacheClient;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.entity.User;
import com.mbv.persist.enums.AccountType;
import com.sun.jersey.api.core.InjectParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by arindamnath on 04/03/16.
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public abstract class BaseAPIServiceImpl {

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private HttpServletRequest httpServletRequest;

    @Context
    private HttpServletResponse httpServletResponse;

    @Context
    private Request request;

    @Context
    private ServletContext servletContext;

    @Context
    private ServletConfig servletConfig;

    @Context
    private UriInfo uriInfo;

    @InjectParam
    UserDAO userDAO;

    @Autowired
    private CacheClient cacheClient;

    public CacheClient getCacheClient() {
        return cacheClient;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public Request getRequest() {
        return request;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    protected UserContext getUserContext(){
        return (UserContext) SecurityContextHolder.getContext().getAuthentication();
    }

    protected String getIpFromRequest() {
        String remoteIP = httpServletRequest.getHeader("X-FORWARDED-FOR");
        if(!StringUtil.isNullOrEmpty(remoteIP)){
            return remoteIP;
        }
        return httpServletRequest.getRemoteAddr();
    }

    protected Map<Long,AccountType> getUserAccountRoles(){
        Map<Long,AccountType> roleMap = new HashMap<Long,AccountType>();
        Map<Long,AccountType> accountType = this.getUserContext().getDetails();
        for(Map.Entry<Long,AccountType> entry: accountType.entrySet()){
            roleMap.put(entry.getKey(),entry.getValue());
        }
        return roleMap;
    }

    protected void checkAccess(Long userId){
        if (userId == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Map<Long,AccountType> userAcctRoles =  getUserAccountRoles();
        if(!userAcctRoles.containsKey(userId) )
            if(!this.getUserContext().getSwitchAccountDetails().containsKey(userId)) {
                if (!userAcctRoles.containsKey(userId) && !this.getUserContext().getSwitchAccountDetails().containsKey(userId))
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
    }

    protected Map<Object, Object> getFilters(String filter){
        Map<Object, Object> filters = new HashMap<Object, Object>();

        if(StringUtil.isNullOrEmpty(filter)){
            return filters;
        }

        //eg., advertisers:9182,8373+status:4,9
        String[] filterList = filter.split(";");
        for(String indFilter : filterList){
            String key = indFilter.split(":")[0];
            String[] values = indFilter.split(":")[1].split(",");
            filters.put((Object) key, (Object) Arrays.asList(values));
        }
        return filters;
    }

    protected List<String> getResponseFilters(String filter){
        List<String> responseFilterList = new ArrayList<String>();
        if(StringUtil.isNullOrEmpty(filter)){
            return responseFilterList;
        }
        for(String indFilter : filter.split(",")){
            responseFilterList.add(indFilter.trim());
        }
        return responseFilterList;
    }
}
