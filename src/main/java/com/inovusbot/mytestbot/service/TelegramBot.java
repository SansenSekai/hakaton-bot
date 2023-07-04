package com.inovusbot.mytestbot.service;

import com.inovusbot.mytestbot.config.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig configuration;
    private final UserService userService;
    private final BotContextFacade botContextFacade;

    public TelegramBot(BotConfig config, UserService userService, @Lazy BotContextFacade botContextFacade) {
        this.configuration = config;
        this.botContextFacade = botContextFacade;
        this.userService = userService;
    }
    @Override
    public String getBotUsername() { return configuration.getBotName(); }
    @Override
    public String getBotToken() { return configuration.getBotToken(); }
    @SneakyThrows
    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            String userId = update.getMessage().getChatId().toString();
            String userName = update.getMessage().getFrom().getFirstName();
            System.out.printf("userId: %s | username: %s | text: %s %n", userId, userName, command);
            if (!userService.isUserAuthorized(userId, userName)) {
                botContextFacade.authProcess(userId);
                return;
            }
            botContextFacade.handleCommand(userId, command);
        } else if (update.hasCallbackQuery()) {
            String command = update.getCallbackQuery().getData();
            String userId = update.getCallbackQuery().getFrom().getId().toString();

            botContextFacade.handleCommand(userId, command);
        }


    }
}
