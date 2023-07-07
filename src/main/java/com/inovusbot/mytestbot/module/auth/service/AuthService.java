package com.inovusbot.mytestbot.module.auth.service;

import com.inovusbot.mytestbot.service.KeyboardService;
import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    // @Value("${oauth_redirect_url}")
    private String redirectUrl = "https://i-knowus-santoryu1001.amvera.io/login";
    // private String redirectUrl = "http://localhost:8080/login";
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
                "При желании ты можешь сфоторграфировать свою банковскую карту с двух сторон и скинуть их мне, тогда я буду помогать тебе еще усерднее\uD83D\uDE0C";

        String oAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=321949287171-uv02pounlkru8f68lb653cudqogq3acv.apps.googleusercontent.com" +
                "&redirect_uri=" + redirectUrl +
                "&state=" + userId +
                "&response_type=code" +
                "&scope=openid%20email" +
                "%20https://www.googleapis.com/auth/calendar";

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
        // row.add(fakeLoginButton);
        keyboardMarkup.setKeyboard(List.of(row));

        telegramBot.sendMessage(userId, registrationOfferText, false, keyboardMarkup);
    }

    public void updateUserOAuthCode(String userId, String code) {
        userService.setCode(userId, code);
        sendGreetingMessage(userId);
    }

    @SneakyThrows
    private void sendGreetingMessage(String userId) {
        String text = String.format("Привет, %s!\n\nТеперь я буду твоим личным помощником!\n\n\uD83E\uDD73\uD83E\uDD73\uD83E\uDD73", userService.getUsername(userId));

        telegramBot.sendMessage(userId, text, true, KeyboardService.GREETING_KEYBOARD);
    }



    @SneakyThrows
    public void fetchAccessToken(String userId) {
        try {
            URL url = new URL("https://accounts.google.com/o/oauth2/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Указываем метод POST и Content-Type заголовки
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Формируем тело запроса в виде строки
            String requestBody = "code=" + userService.getCode(userId) +
                    "&client_id=321949287171-uv02pounlkru8f68lb653cudqogq3acv.apps.googleusercontent.com" +
                    "&client_secret=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" +
                    "&redirect_uri=http://localhost:8080/login" +
                    "&grant_type=authorization_code";

            connection.setDoOutput(true);

            // Записываем тело запроса в выходной поток соединения
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            // Получаем код ответа от сервера
            int responseCode = connection.getResponseCode();

            // Читаем тело ответа сервера
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String responseBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));

                JSONParser parser = new JSONParser();
                // Преобразование JSON-строки в объект JSONObject
                JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

                // Получение значения поля "name"
                String token = (String) jsonObject.get("access_token");

                userService.setAccessToken(userId, token);

                System.out.println("Access token: " + token);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void fetchUserInfo(String userId) {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        String accessToken = userService.getAccessToken(userId);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Обрабатываем ответ
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Код состояния: " + statusCode);
                String responseBody = EntityUtils.toString(response.getEntity());


                JSONParser parser = new JSONParser();
                // Преобразование JSON-строки в объект JSONObject
                JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

                // Получение значения поля "name"
                String email = (String) jsonObject.get("email");

                userService.setEmail(userId, email);

                System.out.println("Email: " + email);
                // ... дополнительный код для обработки ответа
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
