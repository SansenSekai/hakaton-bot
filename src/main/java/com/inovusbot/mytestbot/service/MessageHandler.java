package com.inovusbot.mytestbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageHandler {
    private final String START_COMMAND = "/start";

    public BotApiMethod<?> handle(Update update) {
        Message incomingMessage = update.getMessage();
        String text = incomingMessage.getText();
        if (START_COMMAND.equals(text)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
            sendMessage.setText("Hello world!");
            return sendMessage;
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(incomingMessage.getChatId()));
            sendMessage.setText("Wrong Command");
            return sendMessage;
        }
    }
}
