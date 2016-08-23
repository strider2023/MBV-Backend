package com.mbv.baseapp.bootstrap;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.glassfish.grizzly.GrizzlyFuture;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.servlet.FilterRegistration;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;


public abstract class BaseSpringWebApplication {

	private Context context;
	
	private ServletRegistration restServlet;
	
	private HttpServer server;
	
	private Thread shutDownTask;

	private boolean initialized = Boolean.FALSE;
	
	public abstract String applicationName();
	
	public abstract void configure();
	
	public String hostName(){
		return NetworkListener.DEFAULT_NETWORK_HOST;
	}

	public void addFilter(String filterName, String path, Class<? extends Filter> filterClass){
		FilterRegistration testFilterReg = context.addFilter(filterName, filterClass);
        testFilterReg.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), path);
	}
	
	public void addInitParameter(String key, String value){
		restServlet.setInitParameter(key, value);
	}
	
	public void addStaticHandler(String contextPath, String... sourcePath){
		server.getServerConfiguration().addHttpHandler(new StaticHttpHandler(sourcePath), contextPath);
	}
	
	public void addJarHandler(String contextPath, String jarPath) throws MalformedURLException{
		server.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(new URLClassLoader(new URL[] {new URL(jarPath)})), contextPath);
	}
	
	protected void initialize() {
    	context = new Context(applicationName());
    	context.addContextInitParameter("contextConfigLocation", applicationName()+"-context.xml");
    	context.addListener("org.springframework.web.context.ContextLoaderListener");
    	context.addListener("org.springframework.web.context.request.RequestContextListener");
    	SpringServlet servlet = new SpringServlet();
    	
    	restServlet = context.addServlet("spring",servlet);
    	restServlet.addMapping("/*");
    	
    	server = new HttpServer();
    	NetworkListener listener = new NetworkListener(applicationName(), hostName(), 8080);

    
    	server.addListener(listener);
    	
    	configure();
    	initialized = Boolean.TRUE;
	}
	
	public void start() throws Exception{
		if(!initialized){
			initialize();
		}
		
    	context.deploy(server);
    	if(context.deployed()){
    		server.start();
        	shutDownTask = new ShutdownTask();
            Runtime.getRuntime().addShutdownHook(shutDownTask);
            Thread.currentThread().join();
    	}
    	else{
    		throw new Exception("Application failed to start..check logs.");
    	}
    }
	
	public class ShutdownTask extends Thread{
		@Override
		public void run() {
			GrizzlyFuture<HttpServer> future = server.shutdown();
			try {
				if(future.get(5, TimeUnit.SECONDS).isStarted())
					server.shutdownNow();
			}
			catch (Exception e) {
				e.printStackTrace();
				server.shutdownNow();
			}
		}
	}
	
	public class Context extends WebappContext{
		
		public Context() {
			super();
		}

		public Context(String displayName, String contextPath, String basePath) {
			super(displayName, contextPath, basePath);
		}

		public Context(String displayName, String contextPath) {
			super(displayName, contextPath);
		}

		public Context(String displayName) {
			super(displayName);
		}

		public boolean deployed(){
			return super.deployed;
		}
	}
}
