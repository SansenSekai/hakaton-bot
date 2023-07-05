package com.inovusbot.mytestbot.module.poker.service;

import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class PokerFacade {
    private final PokerService pokerService;
    private final UserService userService;

    public PokerFacade(PokerService pokerService, UserService userService) {
        this.pokerService = pokerService;
        this.userService = userService;
    }

    public void showMenu(String userId) {
        pokerService.showMenu(userId);
    }

    public void handleCommand(String userId, String command) {
        String context = userService.getContext(userId);
        switch (context) {
            case "poker-main": {
                String[] strings = command.split("-");
                if(strings[1].equals("create")) {
                    pokerService.createTable(userId);
                } else if(strings[1].equals("join")) {
                    pokerService.selectRoom(userId, strings[2]);
                }

                break;
            }
            default: {
                break;
            }
        }
    }
}
