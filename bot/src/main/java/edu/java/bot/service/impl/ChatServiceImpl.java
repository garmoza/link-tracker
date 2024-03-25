package edu.java.bot.service.impl;

import edu.java.bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public void registerChat(long id) {

    }
}
