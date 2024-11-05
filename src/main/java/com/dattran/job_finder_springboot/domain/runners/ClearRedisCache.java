package com.dattran.job_finder_springboot.domain.runners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClearRedisCache {
    CacheManager cacheManager;
    RedisTemplate<String, Object> redisTemplate;

    // Run if necessary
    @EventListener(ApplicationReadyEvent.class)
    public void clearAllCaches() {
//        cacheManager.getCacheNames()
//                .parallelStream()
//                .forEach(n -> cacheManager.getCache(n).clear());
//        Set<String> keys = redisTemplate.keys("*");
//        if (keys == null || keys.isEmpty()) {
//            log.info("==========Clear all caches=========");
//        } else {
//            log.info("==========Some caches were not cleared===========");
//        }
    }
}
