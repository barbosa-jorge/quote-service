package com.quotemedia.interview.quoteservice.shared.configs;

import com.quotemedia.interview.quoteservice.security.AppProperties;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class AppCacheConfig  extends CachingConfigurerSupport {

    private static final String EVICTION_POLICY_LRU = "LRU";

    @Autowired
    private AppProperties appProperties;

    @Bean
    public net.sf.ehcache.CacheManager ehCacheManager() {

        // move values to properties
        CacheConfiguration quotesCache = new CacheConfiguration();
        quotesCache.setName("quotes-cache");
        quotesCache.setMemoryStoreEvictionPolicy(EVICTION_POLICY_LRU); // default (Least recently used).
        quotesCache.setMaxEntriesLocalHeap(appProperties.getMaxEntriesLocalHeap());
        quotesCache.setTimeToLiveSeconds(appProperties.getTimeToLiveSeconds()); // 1 min only

        // move values to properties
        CacheConfiguration highestSymbolAskCache = new CacheConfiguration();
        highestSymbolAskCache.setName("highestSymbolAsk-cache");
        highestSymbolAskCache.setMemoryStoreEvictionPolicy(EVICTION_POLICY_LRU); // default (Least recently used).
        highestSymbolAskCache.setMaxEntriesLocalHeap(appProperties.getMaxEntriesLocalHeap());
        highestSymbolAskCache.setTimeToLiveSeconds(appProperties.getTimeToLiveSeconds()); // 1 min only

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(quotesCache);
        config.addCache(highestSymbolAskCache);

        return net.sf.ehcache.CacheManager.newInstance(config);

    }

    @Override
    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

}
