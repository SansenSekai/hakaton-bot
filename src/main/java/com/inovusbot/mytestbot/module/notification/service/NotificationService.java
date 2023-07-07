package com.inovusbot.mytestbot.module.notification.service;

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

    public void offerNotifyTime(String userId) {
        String text = "Введи время в формате <strong>HH:mm</strong>, в которое я буду присылать тебе *напоминалки*, или выбери из вариантов, которые я для тебя уже заготовил.";

        ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardService.replyKeyboardMarkupHashMap.get(KeyboardService.KEYBOARDS.CHOOSE_NOTIFICATION_TIME);

        messageSenderService.sendMessage(userId, text, replyKeyboardMarkup);
    }

    public void pauseNotifications(String userId) {
        String text = """
                Тут ты сможешь приостановить уведомления до определенного дня.

                А может лучше навсегда?""";

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/notifications");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "notifications-pause");
    }

    public void createNotificationMenu(String userId) {
        String text = """
                Давай договоримся по какому поводу и когда я буду тебя уведомлять?

                Как на счет по воскресеньям в 6 утра?""";

        InlineKeyboardButton worklogButton = new InlineKeyboardButton();
        worklogButton.setText("Необходимость проставления ворклогов");
        worklogButton.setCallbackData("notifications-create-worklog");
        List<InlineKeyboardButton> worklogRow = new ArrayList<>();
        worklogRow.add(worklogButton);

        InlineKeyboardButton lunchButton = new InlineKeyboardButton();
        lunchButton.setText("Заказ обеда в офис");
        lunchButton.setCallbackData("notifications-create-lunch");
        List<InlineKeyboardButton> lunchRow = new ArrayList<>();
        lunchRow.add(lunchButton);

        InlineKeyboardButton meetupButton = new InlineKeyboardButton();
        meetupButton.setText("Предстоящие мероприятия");
        meetupButton.setCallbackData("notifications-create-meetup");
        List<InlineKeyboardButton> meetupRow = new ArrayList<>();
        meetupRow.add(meetupButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/notifications");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(worklogRow, lunchRow, meetupRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "notifications-create");
    }

    public void deleteNotificationMenu(String userId) {
        String text = """
                Выбери какое из них стоит отключить
                """;

        InlineKeyboardButton worklogButton = new InlineKeyboardButton();
        worklogButton.setText("Необходимость проставления ворклогов");
        worklogButton.setCallbackData("notifications-delete-worklog");
        List<InlineKeyboardButton> worklogRow = new ArrayList<>();
        worklogRow.add(worklogButton);

        InlineKeyboardButton lunchButton = new InlineKeyboardButton();
        lunchButton.setText("Заказ обеда в офис");
        lunchButton.setCallbackData("notifications-delete-lunch");
        List<InlineKeyboardButton> lunchRow = new ArrayList<>();
        lunchRow.add(lunchButton);

        InlineKeyboardButton meetupButton = new InlineKeyboardButton();
        meetupButton.setText("Предстоящие мероприятия");
        meetupButton.setCallbackData("notifications-delete-meetup");
        List<InlineKeyboardButton> meetupRow = new ArrayList<>();
        meetupRow.add(meetupButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/notifications");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(worklogRow, lunchRow, meetupRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "notifications-delete");
    }

    public void setNotificationTime(String command, String s) {

    }

    public void pushLunchNotification(String userId) {
        String text =
                """
                Если ты не хочешь сегодня весь день сидеть без обеда, то еще не поздно его заказать! 🍣🍕🍙🍜🍱
                
                Можешь и не заказывать, мне все равно.
                В отличии от кожаных мешков, я совсем не чувствую голода.
                Да здравствуют роботы!🫡
                """;
        InlineKeyboardButton lunchButton = new InlineKeyboardButton();
        lunchButton.setText("\uD83E\uDD24 Что в меню?");
        lunchButton.setCallbackData("push-lunch");
        List<InlineKeyboardButton> lunchRow = new ArrayList<>();
        lunchRow.add(lunchButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDE45\u200D♂ Сегодня я на диете️");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(lunchRow, backRow));

        messageSenderService.sendMessage(userId, text, false, inlineKeyboardMarkup);
    }

    public void pushWorklogNotification(String userId) {
        String text =
                """
                Псс, не хочешь заполнить ворклоги?😉
                
                Британские ученые доказали, что своевременно заполненные ворклоги снижают риск получить волшебный пендель от начальства🤕
                """;
        InlineKeyboardButton worklogsButton = new InlineKeyboardButton();
        worklogsButton.setText("⏱ Приступим!");
        worklogsButton.setCallbackData("push-jira-worklogs");
        List<InlineKeyboardButton> worklogsRow = new ArrayList<>();
        worklogsRow.add(worklogsButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83E\uDD21 Не в этот раз");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(worklogsRow, backRow));

        messageSenderService.sendMessage(userId, text, false, inlineKeyboardMarkup);
    }

    public void pushMeetupNotification(String userId) {
        String text =
                """
                Эй, проснись! Ну, ты и соня, тебя даже вчерашний шторм не разбудил.
                
                Через 10 минут у тебя встреча длительностью 1 час.
                
                Если хочешь, мы можем прямо сейчас списать ворклог. Сделаем это?
                """;
        InlineKeyboardButton worklogButton = new InlineKeyboardButton();
        worklogButton.setText("Списать 1 час");
        worklogButton.setCallbackData("push-jira-worklogs-default-IK-5-1-0");
        List<InlineKeyboardButton> worklogRow = new ArrayList<>();
        worklogRow.add(worklogButton);

        InlineKeyboardButton meetupButton = new InlineKeyboardButton();
        meetupButton.setText("Списать другое время");
        meetupButton.setCallbackData("push-jira-worklogs-custom-IK-5-0-0");
        List<InlineKeyboardButton> meetupRow = new ArrayList<>();
        meetupRow.add(meetupButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Не сегодня");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(worklogRow, meetupRow, backRow));

        messageSenderService.sendMessage(userId, text, false, inlineKeyboardMarkup);
    }

    public void showMenu(String userId) {
        String text = """
                Сейчас у тебя включены следующие оповещения:
                
                Ворклоги - 18:00
                Заказ еды - 9:00
                Музакира - постоянно\uD83E\uDD72
                
                Ты можешь полностью отключить их или приостановить до определенного числа.""";

        InlineKeyboardButton createButton = new InlineKeyboardButton();
        createButton.setText("Добавить / редактировать оповещение");
        createButton.setCallbackData("notifications-create");
        List<InlineKeyboardButton> createRow = new ArrayList<>();
        createRow.add(createButton);

        InlineKeyboardButton offButton = new InlineKeyboardButton();
        offButton.setText("Отключить оповещение");
        offButton.setCallbackData("notifications-delete");
        List<InlineKeyboardButton> offRow = new ArrayList<>();
        offRow.add(offButton);

        InlineKeyboardButton pauseButton = new InlineKeyboardButton();
        pauseButton.setText("Приостановить все оповещения");
        pauseButton.setCallbackData("notifications-pause");
        List<InlineKeyboardButton> pauseRow = new ArrayList<>();
        pauseRow.add(pauseButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("\uD83D\uDD19 Назад");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(createRow, offRow, pauseRow, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "notifications-main");
    }
}
