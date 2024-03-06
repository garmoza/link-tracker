package edu.java.bot.client.impl;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractWebClient {

    final WebClient webClient;

    public AbstractWebClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }
}
