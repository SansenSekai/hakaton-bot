package com.inovusbot.mytestbot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inovusbot.mytestbot.config.Commands.ABOUT;
import static com.inovusbot.mytestbot.config.Commands.MENU;

public class KeyboardService {

    public final static InlineKeyboardMarkup MAIN_MENU_INLINE_KEYBOARD = new InlineKeyboardMarkup();
    public final static InlineKeyboardMarkup GREETING_KEYBOARD = new InlineKeyboardMarkup();

    public enum KEYBOARDS {
        YES_NO,
        CHOOSE_NOTIFICATION_TIME,

        NOTIFICATION_MENU;
    }

    public final static Map<KEYBOARDS, InlineKeyboardMarkup> inlineKeyboardMarkupMap = new HashMap<>();
    public final static Map<KEYBOARDS, ReplyKeyboardMarkup> replyKeyboardMarkupHashMap = new HashMap<>();



    @PostConstruct
    private void init() {
        this.initNotificationTimeKeyboard();
        this.initMainMenu();
        this.initGreetingKeyboard();
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

    private void initNotificationMainMenuKeyboard() {

        InlineKeyboardButton switchJiraButton = new InlineKeyboardButton();
        switchJiraButton.setText("Включить напоминания для Jira");
        switchJiraButton.setText("switch_jira_notify");

        InlineKeyboardButton switchLunchButton = new InlineKeyboardButton();
        switchLunchButton.setText("Включить напоминания для заказа обедов");
        switchLunchButton.setText("switch_lunch_notify");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(switchJiraButton);
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(switchLunchButton);
        keyboardMarkup.setKeyboard(List.of(row1, row2));

        inlineKeyboardMarkupMap.put(KEYBOARDS.NOTIFICATION_MENU, keyboardMarkup);
    }

    private void initMainMenu() {
        InlineKeyboardButton notificationsButton = new InlineKeyboardButton();
        notificationsButton.setText("Нотификации");
        notificationsButton.setText("/notifications");
        List<InlineKeyboardButton> notificationsRow = new ArrayList<>();
        notificationsRow.add(notificationsButton);

        InlineKeyboardButton calendarButton = new InlineKeyboardButton();
        calendarButton.setText("Календарь");
        calendarButton.setText("/calendar");
        List<InlineKeyboardButton> calendarRow = new ArrayList<>();
        calendarRow.add(calendarButton);

        InlineKeyboardButton lunchButton = new InlineKeyboardButton();
        lunchButton.setText("Заказ обедов");
        lunchButton.setText("/lunch");
        List<InlineKeyboardButton> lunchRow = new ArrayList<>();
        lunchRow.add(lunchButton);

        InlineKeyboardButton pokerButton = new InlineKeyboardButton();
        pokerButton.setText("Planning Poker");
        pokerButton.setText("/poker");
        List<InlineKeyboardButton> pokerRow = new ArrayList<>();
        pokerRow.add(pokerButton);

        MAIN_MENU_INLINE_KEYBOARD.setKeyboard(List.of(notificationsRow, calendarRow, lunchRow, pokerRow));
    }

    private void initGreetingKeyboard() {
        InlineKeyboardButton aboutButton = new InlineKeyboardButton();
        aboutButton.setText("Расскажи что ты умеешь");
        aboutButton.setCallbackData(ABOUT);
        List<InlineKeyboardButton> aboutRow = new ArrayList<>();
        aboutRow.add(aboutButton);

        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton();
        mainMenuButton.setText("Поехали!");
        mainMenuButton.setCallbackData(MENU);
        List<InlineKeyboardButton> mainMenuRow = new ArrayList<>();
        mainMenuRow.add(aboutButton);

        GREETING_KEYBOARD.setKeyboard(List.of(aboutRow, mainMenuRow));
    }
}
