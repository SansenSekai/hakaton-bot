package com.inovusbot.mytestbot.module.notify;

import com.inovusbot.mytestbot.service.MessageSenderService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    private final MessageSenderService messageSenderService;

    public NotificationService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    public void offerNotifyTime(String userId) {
        String text = "Введи время в формате <strong>HH:mm</strong>, в которое я буду присылать тебе напоминалки, или выбери из вариантов, которые я для тебя уже заготовил.";

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        keyboardRemove.setRemoveKeyboard(true);
        return keyboardRemove;

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();

        KeyboardButton keyboardButton16 = new KeyboardButton("16:00");
        KeyboardButton keyboardButton18 = new KeyboardButton("18:00");
        KeyboardButton keyboardButton20 = new KeyboardButton("20:00");
        KeyboardButton keyboardButton22 = new KeyboardButton("22:00");
        KeyboardButton keyboardButtonCancel = new KeyboardButton("Отменить");

        keyboardRow1.add(keyboardButton16);
        keyboardRow1.add(keyboardButton18);
        keyboardRow1.add(keyboardButton20);
        keyboardRow1.add(keyboardButton22);
        keyboardRow2.add(keyboardButtonCancel);

        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        messageSenderService.sendMessage(userId, text, replyKeyboardMarkup);
    }
}
