package com.mbv.api.auth;

import com.mbv.persist.enums.AccountType;
import com.mbv.persist.enums.Currency;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class UserContext implements Authentication{

	private long userId;

	private String ip;

	private boolean authenticated;

	private Map<Long,AccountType> accountAuthorization;

	private Date createDate;

	private Currency currency;

	private Map<Long,AccountType> roleAuthorization;

	private boolean isAccountEnable ;

    private  Map<Long,AccountType> switchAccountInfo;

	public UserContext(long userId, String ip, Map<Long, AccountType> accountRoles, Currency currency, AccountType accountType, boolean isAccountEnable) {
		this.userId = userId;
		this.ip = ip;
		this.createDate = new Date();
		this.currency = currency;
		accountAuthorization = new HashMap<Long, AccountType>();
		roleAuthorization = new HashMap<Long, AccountType>();
		for (Map.Entry<Long, AccountType> entry : accountRoles.entrySet()) {
			roleAuthorization.put(entry.getKey(), entry.getValue());
			accountAuthorization.put(entry.getKey(), accountType);
		}
		this.isAccountEnable=isAccountEnable;
        switchAccountInfo=new HashMap<Long,AccountType>();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1102175719523392083L;

	@Override
	public String getName() {
		return String.valueOf(userId);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authority = new HashSet<GrantedAuthority>();
		for(AccountType accountType : accountAuthorization.values()){
			authority.add(roleToAuthority(accountType.name()));
		}
		for(AccountType accountType : switchAccountInfo.values()){
			authority.add(roleToAuthority(accountType.name()));
		}
		if(roleAuthorization.values().contains(AccountType.ADMIN)){
			authority.add(roleToAuthority(roleAuthorization.values().iterator().next().name()));
		}
		if(isAccountEnable){
			authority.add(roleToAuthority("ADMIN_ACCESS"));
		}

		return authority;
	}

	@Override
	public Object getCredentials() {
		return this;
	}

	@Override
	public Map<Long,AccountType> getDetails(){
		return accountAuthorization;
	}
	
	public void addRole(Long id, AccountType accountType){
		this.accountAuthorization.put(id, accountType);
	}

	@Override
	public Object getPrincipal() {
		return this;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
	}

	public long getUserId() {
		return userId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getIp() {
		return ip;
	}

	public AccountType getRole(){
		return roleAuthorization.values().iterator().next();
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public boolean isExpired(int validityInSeconds) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(createDate);
		cal.add(Calendar.SECOND,validityInSeconds);
		return System.currentTimeMillis() > cal.getTimeInMillis();
	}
	
	private static GrantedAuthority roleToAuthority(final String roleName){
		return new GrantedAuthority(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return roleName;
			}
		};
	}

	public Map<Long,AccountType> getSwitchAccountDetails(){
		return switchAccountInfo;
	}
}