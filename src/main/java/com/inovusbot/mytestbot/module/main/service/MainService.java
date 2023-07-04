package com.inovusbot.mytestbot.module.main.service;

import com.inovusbot.mytestbot.service.MessageSenderService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.inovusbot.mytestbot.config.Commands.HELP;
import static com.inovusbot.mytestbot.config.Commands.START;

@Service
public class MainService {
    private final MessageSenderService messageSenderService;

    public MainService(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    public void tellAboutBot(String userId) {
        String text = "Я могу всё и даже больше!\n\n" +
                "Подробнее об это расскажут Виталий и Шамиль";

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton helpButton = new InlineKeyboardButton();
        helpButton.setText("Покажи мне все команды");
        helpButton.setCallbackData(HELP);

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Сам разберусь");
        startButton.setCallbackData(START);

        row1.add(helpButton);
        row2.add(startButton);

        rowsInline.add(row1);
        rowsInline.add(row2);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
    }

    public void sendAllCommands(String userId) {
        String text = "Немного из того, что я умею:\n\n" +
                "/restart - вынуждает меня забыть всю информацию о тебе\n\n";

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Приступим!");
        startButton.setCallbackData(START);

        row.add(startButton);
        rowsInline.add(row);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
    }

    @SneakyThrows
    public void sendSimpleMessage(String userId) {
        String text = "Ты молодец, продолжай в том же духе!\n\n" +
                "Как только я пойму чего ты хочешь - я обязательно тебе сообщю.";

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Приступим!");
        startButton.setCallbackData(START);

        row.add(startButton);
        rowsInline.add(row);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        messageSenderService.sendMessage(userId, text, false);
        Thread.sleep(1000L);
        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
    }
}
