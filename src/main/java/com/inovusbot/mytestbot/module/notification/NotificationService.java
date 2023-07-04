package com.inovusbot.mytestbot.module.notification;

import com.inovusbot.mytestbot.service.KeyboardService;
import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    private final MessageSenderService messageSenderService;
    private final UserService userService;

    public NotificationService(MessageSenderService messageSenderService, UserService userService) {
        this.messageSenderService = messageSenderService;
        this.userService = userService;
    }

    public void mainMenu(String userId) {
        String text = "Выбери какие напоминания ты хотел бы получать";


        InlineKeyboardMarkup inlineKeyboardMarkup = KeyboardService.inlineKeyboardMarkupMap.get(KeyboardService.KEYBOARDS.NOTIFICATION_MENU);

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
    }

    public void offerNotifyTime(String userId) {
        String text = "Введи время в формате <strong>HH:mm</strong>, в которое я буду присылать тебе *напоминалки*, или выбери из вариантов, которые я для тебя уже заготовил.";

        ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardService.replyKeyboardMarkupHashMap.get(KeyboardService.KEYBOARDS.CHOOSE_NOTIFICATION_TIME);

        messageSenderService.sendMessage(userId, text, replyKeyboardMarkup);
    }

    public void goToSubMenu(String command, String s) {

    }

    public void setNotificationTime(String command, String s) {

    }

    public void turnOffNotification(String userId) {
        String text = "Сейчас у тебя включены следующие уведомления:\n" +
                "Ворклоги в 18:00\n" +
                "Заказ еды в 9:00\n\n" +
                "Ты можешь полностью отключить их или до определенного числа.";

        InlineKeyboardButton worklogsButton = new InlineKeyboardButton();
        worklogsButton.setText("Посмотреть активные уведомления");
        worklogsButton.setCallbackData("notifications-off-worklogs");
        List<InlineKeyboardButton> worklogsRow = new ArrayList<>();
        worklogsRow.add(worklogsButton);

        InlineKeyboardButton lunchButton = new InlineKeyboardButton();
        lunchButton.setText("Посмотреть активные уведомления");
        lunchButton.setCallbackData("notifications-off-lunch");
        List<InlineKeyboardButton> lunchRow = new ArrayList<>();
        lunchRow.add(lunchButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("/notifications");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(worklogsRow, lunchRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "notifications-off");
    }

    public void showMenu(String userId) {
        String text = "Давай договоримся по какому поводу и когда я буду тебя уведомлять!\n" +
                "Как на счет по воскресеньям в 6 утра?";

        InlineKeyboardButton showButton = new InlineKeyboardButton();
        showButton.setText("Посмотреть активные уведомления");
        showButton.setCallbackData("notifications-show");
        List<InlineKeyboardButton> showRow = new ArrayList<>();
        showRow.add(showButton);

        InlineKeyboardButton createButton = new InlineKeyboardButton();
        createButton.setText("Создать сценарий для уведомлений");
        createButton.setCallbackData("notifications-create");
        List<InlineKeyboardButton> createRow = new ArrayList<>();
        createRow.add(createButton);

        InlineKeyboardButton disableButton = new InlineKeyboardButton();
        disableButton.setText("Отключить все уведомления");
        disableButton.setCallbackData("notifications-stop");
        List<InlineKeyboardButton> disableRow = new ArrayList<>();
        disableRow.add(disableButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("/");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(showRow, createRow, disableRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "notifications-main");
    }
}
