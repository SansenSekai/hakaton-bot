package com.inovusbot.mytestbot.controller;

import com.inovusbot.mytestbot.config.BotConfig;
import com.inovusbot.mytestbot.service.MessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Component
public class BotController extends SpringWebhookBot {
    private final BotConfig configuration;
    private final MessageHandler messageHandler;

    public BotController(SetWebhook setWebhook, String botToken, BotConfig configuration, MessageHandler messageHandler) {
        super(setWebhook, botToken);
        this.configuration = configuration;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return messageHandler.handle(update);
    }

    @Override
    public String getBotPath() {
        return configuration.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return configuration.getBotName();
    }
}
