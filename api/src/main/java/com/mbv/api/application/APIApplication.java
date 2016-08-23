package com.mbv.api.application;

import com.mbv.baseapp.bootstrap.BaseSpringWebApplication;

import org.springframework.web.filter.DelegatingFilterProxy;

public class APIApplication extends BaseSpringWebApplication {

	@Override
	public String applicationName() {
		return "api";
	}

	@Override
	public void configure() {
		this.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature","true");
		this.addInitParameter("com.sun.jersey.config.property.packages","com.mbv.api.svc.impl.rest");
        this.addInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters","com.mbv.api.auth.ResponseCORSFilter");
        this.addFilter("filterChainProxy", "/*", DelegatingFilterProxy.class);
    }	
	
	public static void main(String args[]) throws Exception{
		APIApplication app = new APIApplication();
		app.start();
	}
}
