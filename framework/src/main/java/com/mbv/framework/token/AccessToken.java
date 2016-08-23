package com.mbv.framework.token;

import java.io.Serializable;
import java.sql.Timestamp;


public class AccessToken<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1131077427167067151L;

	private String token;
	
	private Timestamp issueTime;

	private T id;
	
	public AccessToken(T id, String token, Timestamp issueTime) {
		this.id = id;
		this.token = token;
		this.issueTime = issueTime;
	}

	public String getToken() {
		return token;
	}

	public Timestamp getIssueTime() {
		return issueTime;
	}

	public T getId(){
		return id;
	}
}
