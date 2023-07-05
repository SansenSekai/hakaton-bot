package com.inovusbot.mytestbot.module.auth.service;

import com.inovusbot.mytestbot.service.KeyboardService;
import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.inovusbot.mytestbot.config.Commands.ABOUT;

@Service
public class AuthService {
    // @Value("${oauth_redirect_url}")
    //private String redirectUrl = "https://i-knowuss-santoryu1001.amvera.io/login";
    private String redirectUrl = "http://localhost:8080/login";
    private final MessageSenderService telegramBot;
    private final UserService userService;

    @Autowired
    public AuthService(MessageSenderService telegramBot, UserService userService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
    }

    @SneakyThrows
    public void sendOAuthOffer(String userId) {
        String registrationOfferText = "Привет! \n\n" +
                "Для использования моего функционала нам необходимо сделать шаг на встречу друг другу.\n\n" +
                "Покажи мне свой аккаунт Google, а я буду помогать тебе чем смогу.\n\n" +
                "При желании ты можешь поделиться своей банковской картой, тогда я буду помогать тебе еще усерднее\uD83D\uDE0C";

        String oAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=1047452242837-ce2hc04848h9g6vlpmguudjui32rjsa2.apps.googleusercontent.com" +
                "&redirect_uri=" + redirectUrl +
                "&state=" + userId +
                "&response_type=code" +
                "&scope=openid%20email";

        // Создание кнопки при каждом запросе, ее заранее не создать
        InlineKeyboardButton loginButton = new InlineKeyboardButton();
        loginButton.setText("Войти через Google");
        loginButton.setUrl(oAuthUrl);

        InlineKeyboardButton fakeLoginButton = new InlineKeyboardButton();
        fakeLoginButton.setText("Ввести номер карты");
        fakeLoginButton.setCallbackData("button_pressed");

        // Создание разметки с кнопкой
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(loginButton);
        row.add(fakeLoginButton);
        keyboardMarkup.setKeyboard(List.of(row));

        telegramBot.sendMessage(userId, registrationOfferText, false, keyboardMarkup);
    }

    public void updateUserOAuthCode(String googleCode, String userId) {
        userService.setGoogleToken(userId, googleCode);
        sendGreetingMessage(userId);
    }

    @SneakyThrows
    private void sendGreetingMessage(String userId) {
        String text = String.format("Привет, %s!\n\nТеперь я буду твоим личным помощником!\n\n\uD83E\uDD73\uD83E\uDD73\uD83E\uDD73", userService.getUsername(userId));

        telegramBot.sendMessage(userId, text, true, KeyboardService.GREETING_KEYBOARD);
    }

    @SneakyThrows
    public void sendLogout(String userId) {
        String registrationOfferText = "Привет! \n\n" +
                "Для использования моего функционала нам необходимо сделать шаг на встречу друг другу.\n\n" +
                "Покажи мне свой аккаунт Google, а я буду помогать тебе чем смогу.\n\n" +
                "При желании ты можешь поделиться своей банковской картой, тогда я буду помогать тебе еще усерднее\uD83D\uDE0C";

        String oAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=1047452242837-ce2hc04848h9g6vlpmguudjui32rjsa2.apps.googleusercontent.com" +
                "&redirect_uri=" + redirectUrl +
                "&state=" + userId +
                "&response_type=code" +
                "&scope=openid%20email";

        // Создание кнопки при каждом запросе, ее заранее не создать
        InlineKeyboardButton loginButton = new InlineKeyboardButton();
        loginButton.setText("Войти через Google");
        loginButton.setUrl(oAuthUrl);

        InlineKeyboardButton fakeLoginButton = new InlineKeyboardButton();
        fakeLoginButton.setText("Ввести номер карты");
        fakeLoginButton.setCallbackData("button_pressed");

        // Создание разметки с кнопкой
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(loginButton);
        row.add(fakeLoginButton);
        keyboardMarkup.setKeyboard(List.of(row));

        telegramBot.sendMessage(userId, registrationOfferText, false, keyboardMarkup);
    }
}
