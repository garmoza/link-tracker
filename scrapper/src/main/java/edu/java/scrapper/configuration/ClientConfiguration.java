package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.client.impl.BotClientImpl;
import edu.java.scrapper.client.impl.GitHubClientImpl;
import edu.java.scrapper.client.impl.StackOverflowClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ApplicationConfig appConfig;

    @Bean
    public BotClient botClient() {
        return new BotClientImpl(appConfig.client().botApiUrl());
    }

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClientImpl(appConfig.client().githubApiUrl());
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClientImpl(appConfig.client().stackoverflowApiUrl());
    }
}
