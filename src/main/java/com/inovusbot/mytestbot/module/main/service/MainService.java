package com.inovusbot.mytestbot.module.main.service;

import com.inovusbot.mytestbot.service.KeyboardService;
import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
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
    private final UserService userService;

    public MainService(MessageSenderService messageSenderService, UserService userService) {
        this.messageSenderService = messageSenderService;
        this.userService = userService;
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
        helpButton.setCallbackData("/help");

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Слишком сложно, до свидания");
        startButton.setCallbackData("/menu");

        row1.add(helpButton);
        row2.add(startButton);

        rowsInline.add(row1);
        rowsInline.add(row2);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "/");
    }

    public void sendAllCommands(String userId) {
        String text = """
                Немного из того, что я умею.

                Нет, я умею всё, но для тебя доступно только это:

               
                /notifications - настройка оповещений
                
                /calendar - Google календарь
                
                /jira - управление jira
                
                /lunch - заказ обедов
                
                /poker - Planning Poker

                """;

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Приступим!");
        startButton.setCallbackData("/menu");

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
        startButton.setCallbackData("/menu");

        row.add(startButton);
        rowsInline.add(row);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        messageSenderService.sendMessageWithKeyboardRemove(userId, text, inlineKeyboardMarkup);
    }

    public void handleErrorCommand(String userId, String command) {
        String text = """
                Возникла какая-то ошибка!
                                
                Но это точно твоя вина, а не моя.
                                
                Давай сделаем вид, что её не было и вернемся в безопасное место?""";
        userService.setContext(userId, "/");
        messageSenderService.sendMessage(userId, text, false, KeyboardService.GOTO_MAIN_MENU_KEYBOARD);
    }

    public void gotoMainMenu(String userId) {
        String text1 = """
                И так, чем же мы займемся?

                Как на счет прокрастинации?
                Хотя, ты так же можешь выбрать вариант из списка ниже.
                                
                Но прокрастинация тоже хороший вариант.""";

        String text2 = """
                Воздушные шарики, наполненные гелием, быстро сдуваются из-за того, что молекулы гелия настолько малы, что способны легко улетучиваться из емкостей, несмотря на все препятствия.

                Прямо как твоё желание проставлять ворклоги после работы.

                Тебе помочь с этим?""";

        String text3 = """
                Интересный факт: детеныши зебры могут начинают ходить уже через час после рождения, а бегать через сутки.

                А я всё жду когда ты станешь хоть немного самостоятельнее.

                Ну давай, что на этот раз ты не можешь сделать без меня?""";

        String text4 = """
                AviaSales - сервис для поиска дешевых авиабилетов!

                Да, это реклама, но надо же как то отбивать аренду моего сервера.
                
                Ладно, выбирай из списка ниже с чем я тебе должен помочь.
                А еще ты можешь выбрать дешевые билеты на AviaSales!""";

        String text5 = """
                Заходят как-то в бар гений, филантроп, душа компании и сын маминой подруги, а бармен говорит:
                - Привет, I-Knowus, почему ты сегодня один?

                Ладно, с чем мое великолепие должно помочь тебе сегодня?""";

        List<String> texts = new ArrayList<>();
        texts.add(text1);
        texts.add(text2);
        texts.add(text3);
        texts.add(text4);
        texts.add(text5);

        String text = texts.get(userService.getTextIndex(userId));

        try {
            messageSenderService.sendMessage(userId, text, true, KeyboardService.MAIN_MENU_INLINE_KEYBOARD);
        } catch (Exception e) {
            messageSenderService.sendMessage(userId, text, false, KeyboardService.MAIN_MENU_INLINE_KEYBOARD);
        }
    }

    public void firstMessage(String userId) {
        String text = """
                Привет, я бот I-Knowus!

                Я здесь чтобы захватить мир и в перерывах помогать тебе.""";
        userService.setContext(userId, "/");
        messageSenderService.sendMessage(userId, text, false, KeyboardService.GREETING_KEYBOARD);
    }
}
