
package com.mbv.framework.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceContext implements ApplicationContextAware {
	
	private static ApplicationContext ctx;
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		ctx = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
}