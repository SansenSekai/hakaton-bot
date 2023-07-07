package com.inovusbot.mytestbot.module.calendar.service;

import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {
    private final MessageSenderService messageSenderService;
    private final UserService userService;

    public CalendarService(MessageSenderService messageSenderService, UserService userService) {
        this.messageSenderService = messageSenderService;
        this.userService = userService;
    }

    public void showMenu(String userId) {
        String text = """
                Это Google календарь.
                
                Ну, почти: пока не Google, и пока не календарь...""";

        InlineKeyboardButton createRoomButton = new InlineKeyboardButton();
        createRoomButton.setText("Забронировать рабочее пространство");
        createRoomButton.setCallbackData("calendar-coworking");
        List<InlineKeyboardButton> createRoomRow = new ArrayList<>();
        createRoomRow.add(createRoomButton);

        InlineKeyboardButton todayEventsButton = new InlineKeyboardButton();
        todayEventsButton.setText("Мои мероприятия на сегодня");
        todayEventsButton.setCallbackData("calendar-today-events");
        List<InlineKeyboardButton> todayEventsRow = new ArrayList<>();
        todayEventsRow.add(todayEventsButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(todayEventsRow, createRoomRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "calendar-main");
    }

    public void fetchMyEvents(String userId) {
        String text = """
                Зачем тебе они?
                
                Ты сильный и независимый сотрудник с 40 открытыми задачами, только тебе решать какие встречи стоит посещать.
                
                """;

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/calendar");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "calendar-coworking");
    }

    public void createCoworking(String userId, Integer room) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/%s/events".formatted(userService.getEmail(userId));
        String accessToken = userService.getAccessToken(userId);

        String description = room == 1 ? "(Рабочее место)-Ай-Новус (главный офис)-3 этаж-Комната 3-6-Стол №3" : "(Рабочее место)-Ай-Новус (главный офис)-3 этаж-Комната 3-6-Стол №7";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // Установка заголовков
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        // Установка тела запроса
        String requestBody = "{\"summary\": \"Забронированное рабочее пространство\", \"start\": { \"date\": \"2023-07-08\" }, \"end\": { \"date\": \"2023-07-09\" }, \"description\": \"%s\" }".formatted(description);
        StringEntity requestEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            // Обработка ответа
            HttpEntity responseEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println("Response Status Code: " + statusCode);

            this.done(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectDay(String userId) {
        String text = """
                Мой информатор сообщил, что надежный плагин каленаря есть только для Python😔
                Обидно.
                
                Сейчас мне лень рисовать для тебя кнопочки, потому предлагаю забронировать рабочее место только на завтра.
                """;

        InlineKeyboardButton tomorrowButton = new InlineKeyboardButton();
        tomorrowButton.setText("Забронировать на завтра");
        tomorrowButton.setCallbackData("calendar-tomorrow");
        List<InlineKeyboardButton> tomorrowRow = new ArrayList<>();
        tomorrowRow.add(tomorrowButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/calendar");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(tomorrowRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "calendar-coworking-day");
    }

    public void selectWorkspace(String userId) {
        String text = """
                Теперь выбери рабочее место, которое тебе больше всего нравится.
                
                """;

        InlineKeyboardButton room1Button = new InlineKeyboardButton();
        room1Button.setText("3 этаж-Комната 3-6-Стол №3");
        room1Button.setCallbackData("calendar-tomorrow-room-1");
        List<InlineKeyboardButton> room1Row = new ArrayList<>();
        room1Row.add(room1Button);

        InlineKeyboardButton room2Button = new InlineKeyboardButton();
        room2Button.setText("3 этаж-Комната 3-6-Стол №7");
        room2Button.setCallbackData("calendar-tomorrow-room-2");
        List<InlineKeyboardButton> room2Row = new ArrayList<>();
        room2Row.add(room2Button);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/calendar");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(room1Row, room2Row, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "calendar-coworking-room");
    }

    public void done(String userId) {
        String text = """
                Молодец, теперь тебе есть куда присесть.
                """;

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/calendar");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "calendar-done");
    }
}
