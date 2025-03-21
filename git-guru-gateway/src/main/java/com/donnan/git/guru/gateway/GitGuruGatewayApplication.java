package com.donnan.git.guru.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication(scanBasePackages = "com.donnan.git.guru.gateway")
public class GitGuruGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitGuruGatewayApplication.class, args);
    }

}

