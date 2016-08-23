package com.mbv.framework.cache;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.AddrUtil;

public class CacheConfig {
	
	private boolean enabled;
	
	private List<InetSocketAddress> serverList;
	
	private int maxReconnectDelay;
	
	private int operationQueueLength;
	
	private int compressionThreshold;
	
	private String nodeLocatorPolicy;
	
	private long operationTimeout;
	
	private long maxBlockTime;
	
	public CacheConfig(){
		this("127.0.0.1:11211");
	}
	
	public CacheConfig(String serverList){
		this(serverList,30,20000,16384,"ketama",2500,10000);
	}
	
	public CacheConfig(String serverList, int maxReconnectDelay, int operationQueueLength, int compressionThreshold,
			String nodeLocatorPolicy, long operationTimeout, long maxBlockTime) {
		super();
		this.enabled = true;
		this.serverList = getAddresses(serverList);
		this.maxReconnectDelay = maxReconnectDelay;
		this.operationQueueLength = operationQueueLength;
		this.compressionThreshold = compressionThreshold;
		this.nodeLocatorPolicy = nodeLocatorPolicy;
		this.operationTimeout = operationTimeout;
		this.maxBlockTime = maxBlockTime;
	}

	public boolean isEnabled() {
    	return enabled;
    }

	public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    }

	List<InetSocketAddress> getServerAddresses() {
		List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
		addresses.addAll(serverList);
		return addresses;
	}
	
	public void setServerList(String serverList) {
		this.serverList = getAddresses(serverList);
	}
	
	int getMaxReconnectDelay() {
		return maxReconnectDelay;
	}
	
	public void setMaxReconnectDelay(int maxReconnectDelay) {
		this.maxReconnectDelay = maxReconnectDelay;
	}
	
	String getNodeLocatorPolicy() {
		return nodeLocatorPolicy;
	}
	
	public void setNodeLocatorPolicy(String nodeLocatorPolicy) {
		this.nodeLocatorPolicy = nodeLocatorPolicy;
	}
	
	int getOperationQueueLength() {
		return operationQueueLength;
	}
	
	public void setOperationQueueLength(int operationQueueLength) {
		this.operationQueueLength = operationQueueLength;
	}
	
	int getCompressionThreshold() {
		return compressionThreshold;
	}
	
	public void setCompressionThreshold(int compressionThreshold) {
		this.compressionThreshold = compressionThreshold;
	}
	
	public long getOperationTimeout() {
		return operationTimeout;
	}
	
	public void setOperationTimeout(long operationTimeout) {
		this.operationTimeout = operationTimeout;
	}

	public long getMaxBlockTime() {
		return maxBlockTime;
	}

	public void setMaxBlockTime(long maxBlockTime) {
		this.maxBlockTime = maxBlockTime;
	}

	private List<InetSocketAddress> getAddresses(String list) {
		String servers = "";
		String[] serverList = list.split(",");
		for (int i = 0; i < serverList.length; i++) {
			if (i != 0) {
				servers += " ";
			}
			servers += serverList[i];
		}
		List<InetSocketAddress> addrs = AddrUtil.getAddresses(servers);
		return addrs;
	}
}
