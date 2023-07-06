package com.inovusbot.mytestbot.module.lunch.service;

import com.inovusbot.mytestbot.module.main.service.MainService;
import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class LunchFacade {
    private final UserService userService;
    private final LunchService lunchService;
    private final MainService mainService;

    public LunchFacade(UserService userService, LunchService lunchService, MainService mainService) {
        this.userService = userService;
        this.lunchService = lunchService;
        this.mainService = mainService;
    }

    public void showMenu(String userId) {
        lunchService.showMenu(userId);
    }

    public void handleCommand(String userId, String command) {
        String context = userService.getContext(userId);
        switch (context) {
            case "lunch-main": {
                String[] parts = command.split("-");
                String day = parts[2];
                lunchService.showMenuMenu(userId, day);
                break;
            }
            case "lunch-menu": {
                System.out.println("do nothing");
                break;
            }
            default: {
                mainService.handleErrorCommand(userId, command);
            }
        }
    }

    public void handlePush(String userId, String command) {
        lunchService.showMenuMenu(userId, "1");
    }
}
