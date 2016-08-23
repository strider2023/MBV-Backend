
package com.mbv.framework.cache;

import com.mbv.framework.spring.ServiceContext;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionObserver;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.HashAlgorithm;
import net.spy.memcached.KetamaNodeLocator;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedNode;
import net.spy.memcached.NodeLocator;
import net.spy.memcached.OperationFactory;
import net.spy.memcached.protocol.binary.BinaryOperationFactory;
import net.spy.memcached.transcoders.SerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CacheClientImpl implements CacheClient, ConnectionObserver{
	
	private static final Logger logger = LoggerFactory.getLogger(CacheClientImpl.class);
	
	public static final String NODE_LOCATOR_KETAMA = "ketama";
	
	private CacheConfig config;
	
	private static final int CACHE_ACCESS_TIMEOUT = 500;
	
	private static final int RETRY_COUNT = 2;
	
	private MemcachedClient client;
	
	public CacheClientImpl(){
		try{
			this.config = ServiceContext.getApplicationContext().getBean(CacheConfig.class);
		}
		catch(Exception e){
			this.config = new CacheConfig();
		}
		try{
			init();
		}
		catch(UnresolvedAddressException e){
			logger.error("Error starting cache client", e);
		}
		catch(IOException e){
			logger.warn("Error starting cache client", e);
		}
	}
	
	@Override
	public void connectionEstablished(SocketAddress socketAddress, int reconnectCount) {
		logger.info("Connection to server {} established", socketAddress);
	}
	
	@Override
	// TODO: Make this more intelligent by having state in CacheServer class?
	public void connectionLost(SocketAddress socketAddress) {
		logger.warn("Connection to server {} lost", socketAddress);
	}
	
	@Override
	public CacheConfig config() {
		return config;
	}
	
	@Override
	public boolean enabled() {
		return config.isEnabled();
	}
	
	public void init() throws IOException {
		if(!enabled())
			return;
		
		stop();
		
		final String nodeLocatorPolicy = config.getNodeLocatorPolicy();
		final long maxReconnectDelay = config.getMaxReconnectDelay();
		final int operationQueueLength = config.getOperationQueueLength();
		final int compressionThreshold = config.getCompressionThreshold();
		final long operationTimeout = config.getOperationTimeout();
		final long maxBlockTime = config.getMaxBlockTime();
		
		final HashAlgorithm hashAlgorithm = NODE_LOCATOR_KETAMA.equals(nodeLocatorPolicy) ? DefaultHashAlgorithm.KETAMA_HASH
		        : DefaultConnectionFactory.DEFAULT_HASH;
		final ConnectionFactory factory = new DefaultConnectionFactory(operationQueueLength, DefaultConnectionFactory.DEFAULT_READ_BUFFER_SIZE){
			
			@Override
			public Transcoder<Object> getDefaultTranscoder() {
				final SerializingTranscoder transcoder = new SerializingTranscoder();
				transcoder.setCompressionThreshold(compressionThreshold);
				return transcoder;
			}
			
			@Override
			public NodeLocator createLocator(final List<MemcachedNode> nodes) {
				if(NODE_LOCATOR_KETAMA.equals(nodeLocatorPolicy)){
					return new KetamaNodeLocator(nodes, getHashAlg());
				}
				else{
					return super.createLocator(nodes);
				}
			}
			
			@Override
			public long getMaxReconnectDelay() {
				return maxReconnectDelay;
			}
			
			@Override
			public HashAlgorithm getHashAlg() {
				return hashAlgorithm;
			}
			
			@Override
			public long getOperationTimeout() {
				return operationTimeout;
			}
			
			@Override
			public long getOpQueueMaxBlockTime() {
				return maxBlockTime;
			}
			
			@Override
			public int getTimeoutExceptionThreshold() {
				return 5;
			}
			
			@Override
			public OperationFactory getOperationFactory() {
				return new BinaryOperationFactory();
			}
			
			@Override
			public boolean isDaemon() {
				return true;
			}
			
		};
		
		client = new MemcachedClient(factory, config.getServerAddresses());
		client.addObserver(this);
		
		
	}
	
	@Override
	public void setAsync(String key, Object val, long ttlMillis, int... timeoutMillis) {
		int writeTimeout = timeoutMillis.length == 1 ? timeoutMillis[0] : CACHE_ACCESS_TIMEOUT;
		set(client, key, val, ttlMillis, writeTimeout, false);
	}
	
	@Override
	public boolean set(String key, Object val, long ttlMillis, int... timeoutMillis) {
		int writeTimeout = timeoutMillis.length == 1 ? timeoutMillis[0] : CACHE_ACCESS_TIMEOUT;
		boolean response = set(client, key, val, ttlMillis, writeTimeout, true);
		return response;
	}
	
	private boolean set(MemcachedClient client, String key, Object val, long ttlMillis, int writeTimeout, boolean shouldWaitForResponse) {
		int retryCount = RETRY_COUNT;
		int iteration = 0;
		while(true){
			iteration += 1;
			try{
				return setWithException(client, key, val, ttlMillis, writeTimeout / iteration, shouldWaitForResponse);
			}
			catch(TimeoutException e){
				if(iteration > retryCount){
					return Boolean.FALSE;
				}
				else{
					logger.info("CacheClient TimeoutException retrying set...", e);
					continue;
				}
			}
			catch(Exception e){
				return Boolean.FALSE;
			}
		}
	}
	
	private boolean setWithException(MemcachedClient client, String key, Object val, long ttlMillis, int writeTimeout, boolean shouldWaitForResponse)
	        throws Exception {
		if(!config.isEnabled())
			return false;
		
		int expSeconds = (int) (ttlMillis / 1000);
		
		if(ttlMillis == MemcachedInfiniteTTL){
			expSeconds = MemcachedInfiniteTTL;
		}
		
		Boolean success = Boolean.FALSE;
		final Future<Boolean> f = client.set(key, expSeconds, val);
		if(shouldWaitForResponse){
			try{
				success = f.get(writeTimeout, TimeUnit.MILLISECONDS);
			}
			catch(TimeoutException e){
				if(f != null)
					f.cancel(false);
				logger.warn("TimeoutException", e);
				throw e;
			}
			catch(Exception e){
				if(f != null)
					f.cancel(false);
				logger.warn("Exception", e);
				throw e;
			}
		}
		else{
			success = Boolean.TRUE;
		}
		return success;
	}
	
	@Override
	public Object get(String key, int... timeoutMillis) {
		logger.debug("Cache lookup for key -{}", key);
		int readTimeout = timeoutMillis.length == 1 ? timeoutMillis[0] : CACHE_ACCESS_TIMEOUT;
		Object object = null;
		if(config.isEnabled()){
			object = get(client, key, readTimeout);
			logger.debug("Returning Key-{}, Value-{}", key, object);
		}
		return object;
	}
	
	private Object get(MemcachedClient client, String key, int readTimeout) {
		int retryCount = RETRY_COUNT;
		int iteration = 0;
		while(true){
			iteration += 1;
			try{
				return getWithException(client, key, readTimeout / iteration);
			}
			catch(TimeoutException e){
				if(iteration > retryCount){
					return null;
				}
				else{
					logger.info("TimeoutException retrying delete...", e);
					continue;
				}
			}
			catch(Exception e){
				return null;
			}
		}
	}
	
	private Object getWithException(MemcachedClient client, String key, int readTimeout) throws Exception {
		Future<Object> f = null;
		Object object = null;
		try{
			f = client.asyncGet(key);
			object = f.get(readTimeout, TimeUnit.MILLISECONDS);
		}
		catch(TimeoutException e){
			if(f != null)
				f.cancel(false);
			logger.warn("CacheClient TimeoutException", e);
			throw e;
		}
		catch(Exception e){
			if(f != null)
				f.cancel(false);
			logger.warn("CacheClient Exception", e);
			throw e;
		}
		return object;
	}
	
	@Override
	public boolean delete(String key, int... timeoutMillis) {
		int writeTimeout = timeoutMillis.length == 1 ? timeoutMillis[0] : CACHE_ACCESS_TIMEOUT;
		return delete(client, key, writeTimeout);
	}
	
	private boolean delete(MemcachedClient client, String key, int readTimeout) {
		int retryCount = RETRY_COUNT;
		int iteration = 0;
		while(true){
			iteration += 1;
			try{
				return deleteWithException(client, key, readTimeout / iteration);
			}
			catch(TimeoutException e){
				if(iteration > retryCount){
					return Boolean.FALSE;
				}
				else{
					logger.info("CacheClient Exception retrying delete...", e);
					continue;
				}
			}
			catch(Exception e){
				return Boolean.FALSE;
			}
		}
	}
	
	private boolean deleteWithException(MemcachedClient client, String key, int writeTimeout) throws Exception {
		Boolean success = Boolean.FALSE;
		Future<Boolean> f = client.delete(key);
		try{
			success = f.get(writeTimeout, TimeUnit.MILLISECONDS);
		}
		catch(TimeoutException e){
			logger.warn("TimeoutException", e);
			f.cancel(false);
			throw e;
		}
		catch(Exception e){
			logger.warn("Exception", e);
			f.cancel(false);
			throw e;
		}
		return success;
	}
	
	@Override
	public void addAsync(String key, Object val, long ttlMillis, int... timeoutMillis) {
		int writeTimeout = timeoutMillis.length == 1 ? timeoutMillis[0] : CACHE_ACCESS_TIMEOUT;
		add(client, key, val, ttlMillis, writeTimeout, false);
	}
	
	@Override
	public boolean add(String key, Object val, long ttlMillis, int... timeoutMillis) {
		int writeTimeout = timeoutMillis.length == 1 ? timeoutMillis[0] : CACHE_ACCESS_TIMEOUT;
		return add(client, key, val, ttlMillis, writeTimeout, true);
	}
	
	private boolean add(MemcachedClient client, String key, Object val, long ttlMillis, int writeTimeout, boolean shouldWaitForResponse) {
		if(!config.isEnabled()){
			return false;
		}
		int expSeconds = (int) (ttlMillis / 1000);
		
		if(ttlMillis == MemcachedInfiniteTTL){
			expSeconds = MemcachedInfiniteTTL;
		}
		
		Boolean success = Boolean.FALSE;
		Future<Boolean> f = client.add(key, expSeconds, val);
		if(shouldWaitForResponse){
			try{
				success = f.get(writeTimeout, TimeUnit.MILLISECONDS);
			}
			catch(TimeoutException e){
				logger.warn("TimeoutException", e);
				f.cancel(false);
			}
			catch(Exception e){
				logger.warn("Exception", e);
				f.cancel(false);
			}
		}
		else{
			success = Boolean.TRUE;
		}
		
		return success;
	}
	
	@Override
	public void flush() {
		try{
			client.flush().get();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		if(client != null)
			client.shutdown(10000, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public Map<String, Object> bulkGet(Set<String> keys, int... timeoutMillis) {
		int readTimeout = timeoutMillis.length == 1 ? timeoutMillis[0] : CACHE_ACCESS_TIMEOUT;
		Map<String, Object> lookupData = null;
		if(config.isEnabled()){
			lookupData = bulkFetchUsingClient(client, keys, readTimeout);
		}
		return lookupData;
	}
	
	public Map<String, Object> bulkFetchUsingClient(MemcachedClient client, Set<String> keys, int readTimeout) {
		Future<Map<String, Object>> bulkGetOpr = null;
		Map<String, Object> result = null;
		try{
			bulkGetOpr = client.asyncGetBulk(keys);
			result = bulkGetOpr.get(readTimeout, TimeUnit.MILLISECONDS);
		}
		catch(TimeoutException e){
			logger.warn("TimeoutException", e);
			bulkGetOpr.cancel(false);
		}
		catch(Exception e){
			logger.warn("Exception", e);
			bulkGetOpr.cancel(false);
		}
		return result;
	}
	
	public Map<String, Object> bulkFetchUsingClient(MemcachedClient client, List<String> keys, long timeoutInMills) {
		Future<Map<String, Object>> bulkGetOpr = null;
		Map<String, Object> result = null;
		try{
			bulkGetOpr = client.asyncGetBulk(keys);
			result = bulkGetOpr.get(timeoutInMills, TimeUnit.MILLISECONDS);
		}
		catch(TimeoutException e){
			logger.warn("TimeoutException", e);
			bulkGetOpr.cancel(false);
		}
		catch(Exception e){
			logger.warn("Exception", e);
			bulkGetOpr.cancel(false);
		}
		return result;
	}
	
}
