package com.mbv.framework.cache;

import java.util.Map;
import java.util.Set;

public interface CacheClient {

	public static final int MemcachedInfiniteTTL = 0;
	
	
    // Set
    public boolean set(String key, Object val, long ttlMillis, int... timeoutMillis);

    public void setAsync(String key, Object val, long ttlMillis, int... timeoutMillis);

    // Add
    public boolean add(String key, Object val, long ttlMillis, int... timeoutMillis);

    public void addAsync(String key, Object val, long ttlMillis, int... timeoutMillis);

    // Remove
    public boolean delete(String key, int... timeoutMillis);

    // Get
    public Object get(String key, int... timeoutMillis);

    // Bulk Get
    public Map<String, Object> bulkGet(Set<String> key, int... timeoutMillis);

    // Cache configuration
    public boolean enabled();

    public CacheConfig config();
    
    // Cache admin
    public void stop();

    public void flush();
    
}
