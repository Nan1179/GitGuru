package com.wx.git.guru.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class GitGuruGatewayApplication {

    @SpringBootApplication(scanBasePackages = "com.wx.git.guru.gateway")
    public class NfTurboGatewayApplication {

        public static void main(String[] args) {
            SpringApplication.run(NfTurboGatewayApplication.class, args);
        }

    }
}
