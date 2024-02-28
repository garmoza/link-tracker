package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {

    private final ApplicationConfig appConfig;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(appConfig.telegramToken());
    }
}
