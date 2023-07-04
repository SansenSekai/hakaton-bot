package com.inovusbot.mytestbot.module.notify;

import com.inovusbot.mytestbot.service.KeyboardService;
import com.inovusbot.mytestbot.service.MessageSenderService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class NotificationService {
    private final MessageSenderService messageSenderService;

    public NotificationService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    public void offerNotifyTime(String userId) {
        String text = "Введи время в формате <strong>HH:mm</strong>, в которое я буду присылать тебе *напоминалки*, или выбери из вариантов, которые я для тебя уже заготовил.";

        ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardService.replyKeyboardMarkupHashMap.get(KeyboardService.KEYBOARDS.CHOOSE_NOTIFICATION_TIME);

        messageSenderService.sendMessage(userId, text, replyKeyboardMarkup);
    }
}
