package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.ResponseService;
import java.util.List;

public class Bot implements UpdatesListener {

    private final TelegramBot bot;
    private final ResponseService responseService;

    public Bot() {
        bot = new TelegramBot(System.getenv("TELEGRAM_API_KEY"));
        responseService = new ResponseService();
    }

    public void start() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            var request = responseService.getResponseRequest(update);
            bot.execute(request);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
