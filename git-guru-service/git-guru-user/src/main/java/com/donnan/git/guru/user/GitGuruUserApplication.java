package com.donnan.git.guru.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Donnan
 */
@SpringBootApplication(scanBasePackages = "com.donnan.git.guru.user")
@EnableDubbo
public class GitGuruUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitGuruUserApplication.class, args);
    }
}
