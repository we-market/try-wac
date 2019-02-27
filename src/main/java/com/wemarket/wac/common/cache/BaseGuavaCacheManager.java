package com.wemarket.wac.common.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * package guava cache manager
 **/
public class BaseGuavaCacheManager extends AbstractCacheManager {

    private Map<String, GuavaCache> cachesMap = new HashMap<>();

    public BaseGuavaCacheManager(Map<String, String> configMap) {
        configMap.entrySet().stream().forEach((Map.Entry<String, String> obj)
                -> cachesMap.put(obj.getKey(),new GuavaCache(obj.getKey(),
                CacheBuilder.from(obj.getValue()).build())));
    }

    /**
     * Specify the collection of Cache instances to use for this CacheManager。
     * @param cachesMap
     */
    public void setCaches(Map<String, GuavaCache> cachesMap) {
        this.cachesMap = cachesMap;
    }

    /**
     * load cacheMap
     */
    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.cachesMap.values();
    }

    public GuavaCache getCache(String name) {
        return this.cachesMap.get(name);
    }
}
