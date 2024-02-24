package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.impl.GitHubClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ApplicationConfig appConfig;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClientImpl(appConfig.client().gitHubApiUrl());
    }
}
