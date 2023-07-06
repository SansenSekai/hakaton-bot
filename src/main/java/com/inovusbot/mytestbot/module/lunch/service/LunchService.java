package com.inovusbot.mytestbot.module.lunch.service;

import com.inovusbot.mytestbot.module.lunch.dto.MenuDTO;
import com.inovusbot.mytestbot.module.lunch.dto.MenuItem;
import com.inovusbot.mytestbot.service.MessageSenderService;
import com.inovusbot.mytestbot.service.UserService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class LunchService {
    private List<MenuDTO> lunches = new ArrayList<>();
    private final MessageSenderService messageSenderService;
    private final UserService userService;

    public LunchService(MessageSenderService messageSenderService, UserService userService) {
        this.messageSenderService = messageSenderService;
        this.userService = userService;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        MenuItem menuItem1 = new MenuItem("1",
                """
                        №3
                        САЛАТ+ГОРЯЧЕЕ
                        Салат "Флоренция" (куриная грудка, кукуруза, перец болгарский, огурец, рис, томат, сметана)
                        Фрикадельки из индейки с гречкой и овощами""",
                "250 ₽");
        MenuItem menuItem2 = new MenuItem("2",
                """
                        №3
                        САЛАТ+ГОРЯЧЕЕ
                        Салат "Флоренция" (куриная грудка, кукуруза, перец болгарский, огурец, рис, томат, сметана)
                        Курица карри с овощами на пару""",
                "250 ₽");
        MenuItem menuItem3 = new MenuItem("2",
                """
                        №3
                        САЛАТ+ГОРЯЧЕЕ
                        Салат "Каприз" (куриная грудка, перец болгарский, фасоль красная, кукуруза, сметанный соус)
                        Спагетти с курочкой и грибами в сливочном соусе""",
                "250 ₽");
        MenuItem menuItem4 = new MenuItem("2",
                """
                        №1
                        САЛАТ+СУП+ГОРЯЧЕЕ
                        Салат "Флоренция" (куриная грудка, кукуруза, перец болгарский, огурец, рис, томат, сметана)
                        Царская уха с красной рыбой
                        Курица карри с овощами на пару""",
                "250 ₽");
        MenuItem menuItem5 = new MenuItem("2",
                """
                        №2
                        СУП+ГОРЯЧЕЕ
                        Царская уха с красной рыбой
                        Курица карри с овощами на пару""",
                "250 ₽");

        MenuDTO menuDTO1 = new MenuDTO();
        menuDTO1.setDate("2023-07-10");
        menuDTO1.setMenuItems(List.of(menuItem1, menuItem2, menuItem3, menuItem4));

        MenuDTO menuDTO2 = new MenuDTO();
        menuDTO2.setDate("2023-07-11");
        menuDTO2.setMenuItems(List.of(menuItem5, menuItem3, menuItem4, menuItem1));

        lunches.add(menuDTO1);
        lunches.add(menuDTO2);
    }

    public void showMenu(String userId) {
        String text = """
                Выбери дату.
                """;

        InlineKeyboardButton day1Button = new InlineKeyboardButton();
        day1Button.setText("Понедельник");
        day1Button.setCallbackData("lunch-day-1");
        List<InlineKeyboardButton> day1Row = new ArrayList<>();
        day1Row.add(day1Button);

        InlineKeyboardButton day2Button = new InlineKeyboardButton();
        day2Button.setText("Вторник");
        day2Button.setCallbackData("lunch-day-2");
        List<InlineKeyboardButton> day2Row = new ArrayList<>();
        day2Row.add(day2Button);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("/menu");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(day1Row, day2Row, backRow));

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "lunch-main");
    }

    public void showMenuMenu(String userId, String day) {
        String text = """
                Выбирай блюдо.
                """;

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        MenuDTO menuDTO = lunches.get(Integer.parseInt(day));

        menuDTO.getMenuItems().forEach(menuItem -> {
            InlineKeyboardButton menuButton = new InlineKeyboardButton();
            menuButton.setText(menuItem.getName());
            menuButton.setCallbackData("do-nothing");
            InlineKeyboardButton minusButton = new InlineKeyboardButton();
            minusButton.setText("➖");
            minusButton.setCallbackData("do-nothing");
            InlineKeyboardButton countButton = new InlineKeyboardButton();
            countButton.setText("0");
            countButton.setCallbackData("do-nothing");
            InlineKeyboardButton plusButton = new InlineKeyboardButton();
            plusButton.setText("➕");
            plusButton.setCallbackData("do-nothing");
            List<InlineKeyboardButton> menuRow = new ArrayList<>();
            List<InlineKeyboardButton> buyRow = new ArrayList<>();
            menuRow.add(menuButton);
            buyRow.add(minusButton);
            buyRow.add(countButton);
            buyRow.add(plusButton);
            buttons.add(menuRow);
            buttons.add(buyRow);
        });

        InlineKeyboardButton buyButton = new InlineKeyboardButton();
        buyButton.setText("Заказать");
        buyButton.setCallbackData("do-nothing");
        List<InlineKeyboardButton> buyRow = new ArrayList<>();
        buyRow.add(buyButton);

        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("/lunch");
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        backRow.add(backButton);

        buttons.add(buyRow);
        buttons.add(backRow);

        inlineKeyboardMarkup.setKeyboard(buttons);

        messageSenderService.sendMessage(userId, text, true, inlineKeyboardMarkup);
        userService.setContext(userId, "lunch-menu");
    }
}
