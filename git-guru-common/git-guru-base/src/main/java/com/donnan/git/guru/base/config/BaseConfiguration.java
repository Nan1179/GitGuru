package com.donnan.git.guru.base.config;

import com.donnan.git.guru.base.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用模块的配置类
 *
 * @author Donnan
 */
@Configuration
public class BaseConfiguration {

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
