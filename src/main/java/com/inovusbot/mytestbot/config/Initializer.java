package com.inovusbot.mytestbot.config;

import com.inovusbot.mytestbot.controller.BotController;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class Initializer {
    @Autowired
    private BotController bot;

    @SneakyThrows
    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot((LongPollingBot) bot);
    }
}
