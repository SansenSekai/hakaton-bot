package com.inovusbot.mytestbot.module.calendar.service;

import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
                
                Ну, почти...""";

        InlineKeyboardButton createRoomButton = new InlineKeyboardButton();
        createRoomButton.setText("Забронировать коворкинг");
        createRoomButton.setCallbackData("calendar-coworking");
        List<InlineKeyboardButton> createRoomRow = new ArrayList<>();
        createRoomRow.add(createRoomButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(createRoomRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "calendar-main");
    }

    public void selectCoworking(String userId) {
        String text = """
                Вы зашли в коворкинг в поисках своего стола.
                
                Однако, здесь слишком темно и вы не можете его найти.""";

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

    public void selectDay(String userId) {
        String text = """
                Мой информатор сообщил, что надежный плагин каленаря есть только для Python😔
                Осуждаю.
                
                Сейчас мне лень рисовать для тебя кнопочки, потому предлагаю забронировать либо на сегодня, либо на завтра.
                """;

        InlineKeyboardButton todayButton = new InlineKeyboardButton();
        todayButton.setText("На сегодня");
        todayButton.setCallbackData("calendar-2023.07.07");
        List<InlineKeyboardButton> todayRow = new ArrayList<>();
        todayRow.add(todayButton);

        InlineKeyboardButton tomorrowButton = new InlineKeyboardButton();
        tomorrowButton.setText("На завтра");
        tomorrowButton.setCallbackData("calendar-2023.07.08");
        List<InlineKeyboardButton> tomorrowRow = new ArrayList<>();
        tomorrowRow.add(tomorrowButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("calendar-coworking");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(todayRow, tomorrowRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "calendar-coworking");
    }
}
