package com.donnan.git.guru.business.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Donnan
 */
@Configuration
public class GitHubUserClientConfiguration {

    @Bean
    public GitHubUserClientConfiguration gitHubUserClient() {
        return new GitHubUserClientConfiguration();
    }
}
