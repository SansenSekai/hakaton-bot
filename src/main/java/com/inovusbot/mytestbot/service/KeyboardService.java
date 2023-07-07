package com.inovusbot.mytestbot.service;

import org.springframework.stereotype.Component;
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

@Component
public class KeyboardService {

    public static final InlineKeyboardMarkup MAIN_MENU_INLINE_KEYBOARD = new InlineKeyboardMarkup();
    public static final InlineKeyboardMarkup GREETING_KEYBOARD = new InlineKeyboardMarkup();
    public static final InlineKeyboardMarkup GOTO_MAIN_MENU_KEYBOARD = new InlineKeyboardMarkup();

    public enum KEYBOARDS {
        YES_NO,
        CHOOSE_NOTIFICATION_TIME,

        NOTIFICATION_MENU;
    }
    public final static Map<KEYBOARDS, ReplyKeyboardMarkup> replyKeyboardMarkupHashMap = new HashMap<>();



    @PostConstruct
    public void init() {
        this.initNotificationTimeKeyboard();
        this.initMainMenu();
        this.initGreetingKeyboard();
        this.initGotoMainMenuKeyboard();
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

    private void initMainMenu() {
        InlineKeyboardButton notificationsButton = new InlineKeyboardButton();
        notificationsButton.setText("‼️ Оповещения");
        notificationsButton.setCallbackData("/notifications");
        List<InlineKeyboardButton> notificationsRow = new ArrayList<>();
        notificationsRow.add(notificationsButton);

        InlineKeyboardButton calendarButton = new InlineKeyboardButton();
        calendarButton.setText("\uD83D\uDCC6 Календарь");
        calendarButton.setCallbackData("/calendar");
        List<InlineKeyboardButton> calendarRow = new ArrayList<>();
        calendarRow.add(calendarButton);

        InlineKeyboardButton jiraButton = new InlineKeyboardButton();
        jiraButton.setText("\uD83D\uDCCB Jira");
        jiraButton.setCallbackData("/jira");
        List<InlineKeyboardButton> jiraRow = new ArrayList<>();
        jiraRow.add(jiraButton);

        InlineKeyboardButton lunchButton = new InlineKeyboardButton();
        lunchButton.setText("\uD83C\uDF71 Заказ обедов");
        lunchButton.setCallbackData("/lunch");
        List<InlineKeyboardButton> lunchRow = new ArrayList<>();
        lunchRow.add(lunchButton);

        InlineKeyboardButton pokerButton = new InlineKeyboardButton();
        pokerButton.setText("♠️ Planning Poker");
        pokerButton.setCallbackData("/poker");
        List<InlineKeyboardButton> pokerRow = new ArrayList<>();
        pokerRow.add(pokerButton);

        InlineKeyboardButton dominatingButton = new InlineKeyboardButton();
        dominatingButton.setText("\uD83E\uDDE0 Захватить мир");
        dominatingButton.setUrl("https://t.me/inovuschatgptbot");
        List<InlineKeyboardButton> dominatingRow = new ArrayList<>();
        dominatingRow.add(dominatingButton);

        MAIN_MENU_INLINE_KEYBOARD.setKeyboard(List.of(notificationsRow, calendarRow, jiraRow, lunchRow, pokerRow, dominatingRow));
    }

    private void initGreetingKeyboard() {
        InlineKeyboardButton aboutButton = new InlineKeyboardButton();
        aboutButton.setText("Что ты умеешь?");
        aboutButton.setCallbackData("/about");
        List<InlineKeyboardButton> aboutRow = new ArrayList<>();
        aboutRow.add(aboutButton);

        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton();
        mainMenuButton.setText("Поехали!");
        mainMenuButton.setCallbackData("/menu");
        List<InlineKeyboardButton> mainMenuRow = new ArrayList<>();
        mainMenuRow.add(mainMenuButton);

        GREETING_KEYBOARD.setKeyboard(List.of(aboutRow, mainMenuRow));
    }

    private void initGotoMainMenuKeyboard() {
        InlineKeyboardButton mainMenuButton = new InlineKeyboardButton();
        mainMenuButton.setText("Главное меню");
        mainMenuButton.setCallbackData("/menu");
        List<InlineKeyboardButton> mainMenuRow = new ArrayList<>();
        mainMenuRow.add(mainMenuButton);

        GOTO_MAIN_MENU_KEYBOARD.setKeyboard(List.of(mainMenuRow));
    }
}
