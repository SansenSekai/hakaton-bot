package com.inovusbot.mytestbot.service;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@Lazy
public class MessageSenderService {
    private final TelegramLongPollingBot telegramBot;
    private final UserService userService;

    public MessageSenderService(TelegramLongPollingBot telegramBot, UserService userService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
    }

    public void sendMessage(String userId, String text, boolean replace) {
        this.sendMessage(userId, text, replace, null);
    }

    public void sendMessage(String userId, String text, boolean replace, InlineKeyboardMarkup keyboardMarkup) {
        if(replace) {
            this.updateMessage(userId, text, keyboardMarkup);
        } else {
            this.sendNewMessage(userId, text, keyboardMarkup);
        }
    }

    @SneakyThrows
    private void sendNewMessage(String userId, String text, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(text);
        if (keyboardMarkup != null) {
            // Установка разметки с кнопкой в сообщение
            message.setReplyMarkup(keyboardMarkup);
        }
        Message executedMessage = telegramBot.execute(message);
        userService.setLastMessageId(userId, executedMessage.getMessageId());
    }

    @SneakyThrows
    private void updateMessage(String userId, String text, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageText message = new EditMessageText();
        message.setChatId(userId);
        message.setMessageId(userService.getLastMessageId(userId));
        message.setText(text);
        if (keyboardMarkup != null) {
            // Установка разметки с кнопкой в сообщение
            message.setReplyMarkup(keyboardMarkup);
        }
        telegramBot.execute(message);
    }
}
