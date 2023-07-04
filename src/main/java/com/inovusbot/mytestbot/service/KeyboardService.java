package com.inovusbot.mytestbot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardService {

    public enum KEYBOARDS {
        YES_NO,
        CHOOSE_NOTIFICATION_TIME;
    }

    public final static Map<KEYBOARDS, InlineKeyboardMarkup> inlineKeyboardMarkupMap = new HashMap<>();
    public final static Map<KEYBOARDS, ReplyKeyboardMarkup> replyKeyboardMarkupHashMap = new HashMap<>();



    @PostConstruct
    private void init() {
        this.initNotificationTimeKeyboard();
    }

    private void initNotificationTimeKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

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

        replyKeyboardMarkupHashMap.put(KEYBOARDS.YES_NO, replyKeyboardMarkup);
    }

    private void initAuthOfferKeyboard() {

    }
}
