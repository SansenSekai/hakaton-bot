package com.inovusbot.mytestbot.module.lunch.service;

import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class LunchFacade {
    private final UserService userService;
    private final LunchService lunchService;

    public LunchFacade(UserService userService, LunchService lunchService) {
        this.userService = userService;
        this.lunchService = lunchService;
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

            }
        }
    }

}
