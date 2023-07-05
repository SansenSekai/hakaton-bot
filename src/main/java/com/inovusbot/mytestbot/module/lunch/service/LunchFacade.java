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

}
