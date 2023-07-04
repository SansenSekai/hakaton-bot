package com.inovusbot.mytestbot.module.poker.service;

import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokerService {
    private final MessageSenderService messageSenderService;
    private final UserService userService;

    public PokerService(MessageSenderService messageSenderService, UserService userService) {
        this.messageSenderService = messageSenderService;
        this.userService = userService;
    }

    public void showMenu(String userId) {
        String text = """
                Сыграем в покер!
                
                Выбирай свободную комнату или создай новую и пригласи команду.
                
                Ваши ставки, Господа!""";

        InlineKeyboardButton createRoomButton = new InlineKeyboardButton();
        createRoomButton.setText("Создать комнату");
        createRoomButton.setCallbackData("poker-room-create");
        List<InlineKeyboardButton> createRoomRow = new ArrayList<>();
        createRoomRow.add(createRoomButton);

        InlineKeyboardButton room1Button = new InlineKeyboardButton();
        room1Button.setText("Комната 1. Участников: 2");
        room1Button.setCallbackData("poker-room-1");
        List<InlineKeyboardButton> room1Row = new ArrayList<>();
        room1Row.add(room1Button);

        InlineKeyboardButton room2Button = new InlineKeyboardButton();
        room2Button.setText("Комната 2. Участников: 4");
        room2Button.setCallbackData("poker-room-2");
        List<InlineKeyboardButton> room2Row = new ArrayList<>();
        room2Row.add(room2Button);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("/");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(createRoomRow, room1Row, room2Row, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "poker-main");
    }
}
