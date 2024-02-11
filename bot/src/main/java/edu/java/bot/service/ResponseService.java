package edu.java.bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

public class ResponseService {

    public <T extends BaseRequest<T, R>, R extends BaseResponse> BaseRequest<?, ?> getResponseRequest(Message message) {
        return new SendMessage(message.chat().id(), message.text().toUpperCase());
    }
}
