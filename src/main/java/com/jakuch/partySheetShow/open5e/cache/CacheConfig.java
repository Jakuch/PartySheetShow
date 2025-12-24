package com.jakuch.partySheetShow.open5e.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        var racesAll = new CaffeineCache(
                "racesAll",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .maximumSize(100)
                        .build()
        );

        var classesAll = new CaffeineCache(
                "classesAll",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .maximumSize(100)
                        .build()
        );

        var manager = new SimpleCacheManager();
        manager.setCaches(List.of(racesAll, classesAll));
        return manager;
    }
}
