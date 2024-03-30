package edu.java.bot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.UpdateService;
import edu.java.model.request.LinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService {

    private final TelegramBot telegramBot;

    @Override
    public ResponseEntity<Void> sendUpdate(LinkUpdate dto) {
        String message = "[Link](%s) has been updated (%s)".formatted(dto.url(), dto.description());
        for (long chatId : dto.tgChatIds()) {
            telegramBot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
