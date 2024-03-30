package edu.java.bot.service.impl;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ScrapperClient scrapperClient;

    @Override
    public boolean existsById(long id) {
        try {
            scrapperClient.getChat(id).block();
            return true;
        } catch (WebClientResponseException e) {
            return false;
        }
    }

    @Override
    public void registerChat(long id) {
        scrapperClient.registerChat(id).block();
    }
}
