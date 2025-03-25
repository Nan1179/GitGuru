package com.donnan.git.guru.cache.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 * @author Donnan
 */
@Configuration
@EnableMethodCache(basePackages = "com.donnan.git.guru")
public class CacheConfiguration {
}
