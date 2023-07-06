package com.inovusbot.mytestbot.module.auth.service;

import com.inovusbot.mytestbot.service.KeyboardService;
import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    String code = "4/0AZEOvhXdv2oN8WL1Y8cHlCUjMVCZdWExIKWz3qi_y7ckYz7ywAyKefuD2J3RuIhlzwFjrA";
    String clientId = "1047452242837-ce2hc04848h9g6vlpmguudjui32rjsa2.apps.googleusercontent.com";
    String clientSecret = "URZWgVDNLiFsnJV5t4SJadZY";
    String redirectUri = "http://localhost:8080/login";

    public void getToken() {
        try {
            // Формирование POST запроса к Google для получения токена
            String requestUrl = "https://accounts.google.com/o/oauth2/token";
            String postData = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                    "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                    "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                    "&grant_type=authorization_code";

            System.out.println(postData);

            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.getOutputStream().write(postData.getBytes(StandardCharsets.UTF_8));

            // Чтение ответа от сервера Google
            InputStream responseStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Декодирование ответа и получение токена
            String responseJson = response.toString();
            String accessToken = null;
            if (responseJson.contains("access_token")) {
                int startIndex = responseJson.indexOf("access_token") + 15;
                int endIndex = responseJson.indexOf("\"", startIndex);
                accessToken = responseJson.substring(startIndex, endIndex);
                System.out.println("accessToken = " + accessToken);
            }

            /*// Использование полученного токена для запросов к Google API
            if (accessToken != null) {
                String apiUrl = "https://www.googleapis.com/your_api_endpoint";
                URL apiURL = new URL(apiUrl);
                HttpURLConnection apiConnection = (HttpURLConnection) apiURL.openConnection();
                apiConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                // ... делать запросы к Google API ...

                apiConnection.disconnect();
            }*/

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void sendOAuthOffer(String userId) {
        String registrationOfferText = "Привет! \n\n" +
                "Для использования моего функционала нам необходимо сделать шаг на встречу друг другу.\n\n" +
                "Покажи мне свой аккаунт Google, а я буду помогать тебе чем смогу.\n\n" +
                "При желании ты можешь поделиться своей банковской картой, тогда я буду помогать тебе еще усерднее\uD83D\uDE0C";

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
        row.add(fakeLoginButton);
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

                System.out.println("Access token: " + token);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
