package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.client.impl.ScrapperClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ApplicationConfig appConfig;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClientImpl(appConfig.client().scrapperApiUrl());
    }
}
