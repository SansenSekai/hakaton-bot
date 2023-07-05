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
        backButton.setText("Назад");
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
        backButton.setText("Назад");
        backButton.setCallbackData("/calendar");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
    }
}
