package com.inovusbot.mytestbot.controller;

import com.inovusbot.mytestbot.config.BotConfig;
import com.inovusbot.mytestbot.dto.UserState;
import com.inovusbot.mytestbot.service.MessageHandler;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotController extends TelegramLongPollingBot {

    private Map<String, UserState> activeUsers = new HashMap<>();
    private final BotConfig configuration;
    private final MessageHandler messageHandler;

    public BotController(BotConfig config, MessageHandler messageHandler) {
        this.configuration = config;
        this.messageHandler = messageHandler;
    }
    @Override
    public String getBotUsername() { return configuration.getBotName(); }
    @Override
    public String getBotToken() { return configuration.getBotToken(); }
    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String command = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            String memberName = update.getMessage().getFrom().getFirstName();

            UserState userState;

            if(activeUsers.containsKey(chatId)){
                userState = activeUsers.get(chatId);
            } else {
                userState = new UserState();
                userState.setChatId(chatId);
                userState.setUsername(memberName);
                userState.setAuthorized(false);
                userState.setContext("/");
            }

            activeUsers.put(userState.getChatId(), userState);

            if(userState.getAuthorized()) {
                System.out.println(command);
                if(command.equals("/myGToken")) {
                    sendAnyMessage(userState, "Твой Google Token = " + userState.getGoogleToken());
                    return;
                }
                startBot(userState);
            } else {
                oauth2(userState);
            }
        }
    }

    @SneakyThrows
    private void startBot(UserState userState) {
        SendMessage message = new SendMessage();
        message.setChatId(userState.getChatId());
        message.setText("Извините, но на большее я пока не способен.");
        execute(message);
    }

    @SneakyThrows
    private void oauth2(UserState userState) {
        String url = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=255834796655-vi5edqeosobbn9d5v6b9mlkrgkrjiscn.apps.googleusercontent.com" +
                "&redirect_uri=http://localhost:8080/login" +
                "&state=" + userState.getChatId() +
                "&response_type=code" +
                "&scope=openid%20email";
        SendMessage message = new SendMessage();
        message.setChatId(userState.getChatId());
        message.setText(url);
        execute(message);
    }

    @SneakyThrows
    public void sendGreetingMessage(UserState userState) {
        SendMessage message = new SendMessage();
        message.setChatId(userState.getChatId());
        message.setText(String.format("Привет, %s!\n Ты успешно авторизовался и теперь тебе доступен функционал бота", userState.getUsername()));
        execute(message);
    }

    @SneakyThrows
    public void sendAnyMessage(UserState userState, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(userState.getChatId());
        message.setText(text);
        execute(message);
    }

    public void updateUserOAuthCode(String googleCode, String userId) {
        UserState userState = activeUsers.get(userId);
        userState.setAuthorized(true);
        userState.setGoogleToken(googleCode);
        sendGreetingMessage(userState);
    }
}
