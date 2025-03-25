package com.donnan.git.guru.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Donnan
 */
@SpringBootApplication(scanBasePackages = {"com.donnan.git.guru.auth"})
@EnableDubbo
public class GitGuruAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitGuruAuthApplication.class, args);
    }
}
