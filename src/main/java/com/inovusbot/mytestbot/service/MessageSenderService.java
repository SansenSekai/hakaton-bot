package com.inovusbot.mytestbot.service;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Service
@Lazy
public class MessageSenderService {
    private final TelegramLongPollingBot telegramBot;
    private final UserService userService;

    public MessageSenderService(TelegramLongPollingBot telegramBot, UserService userService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
    }

    public void sendMessage(String userId, String text) {
        this.sendNewMessage(userId, text, null, null);
    }

    // Просто сообщение
    public void sendMessage(String userId, String text, boolean replace) {
        this.sendMessage(userId, text, replace, null, null);
    }

    // Сообщение с инлайн клавиатурой
    public void sendMessage(String userId, String text, boolean replace, InlineKeyboardMarkup inlineKeyboardMarkup) {
        this.sendMessage(userId, text, replace, inlineKeyboardMarkup, null);
    }

    // Сообщение с обычной клавиатурой
    public void sendMessage(String userId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        this.sendMessage(userId, text, false, null, replyKeyboardMarkup);
    }


    private void sendMessage(String userId, String text, boolean replace, InlineKeyboardMarkup inlineKeyboardMarkup, ReplyKeyboardMarkup replyKeyboardMarkup) {
        if(replace) {
            this.updateMessage(userId, text, inlineKeyboardMarkup);
        } else {
            this.sendNewMessage(userId, text, inlineKeyboardMarkup, replyKeyboardMarkup);
        }
    }

    @SneakyThrows
    private void sendNewMessage(String userId, String text, InlineKeyboardMarkup inlineKeyboardMarkup, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(text);
        if (inlineKeyboardMarkup != null) {
            // Установка разметки с кнопкой в сообщение
            message.setReplyMarkup(inlineKeyboardMarkup);
        }
        if (replyKeyboardMarkup != null) {
            // Установка разметки с кнопкой в сообщение
            message.setReplyMarkup(replyKeyboardMarkup);
        }
        if(inlineKeyboardMarkup == null && replyKeyboardMarkup == null) {
            // Удаляем клавиатуру
            message.setReplyMarkup(new ReplyKeyboardRemove());
        }
        Message executedMessage = telegramBot.execute(message);
        userService.setLastMessageId(userId, executedMessage.getMessageId());
    }

    @SneakyThrows
    private void updateMessage(String userId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText message = new EditMessageText();
        message.setChatId(userId);
        message.setMessageId(userService.getLastMessageId(userId));
        message.setText(text);
        if (inlineKeyboardMarkup != null) {
            // Установка разметки с кнопкой в сообщение
            message.setReplyMarkup(inlineKeyboardMarkup);
        }
        telegramBot.execute(message);
    }
}
