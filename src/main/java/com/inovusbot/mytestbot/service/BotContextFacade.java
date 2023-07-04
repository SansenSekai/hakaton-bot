package com.inovusbot.mytestbot.service;

import com.inovusbot.mytestbot.module.auth.service.AuthService;
import com.inovusbot.mytestbot.module.main.service.MainService;
import com.inovusbot.mytestbot.module.notify.NotifyService;
import org.springframework.stereotype.Service;

import static com.inovusbot.mytestbot.config.Commands.*;

@Service
public class BotContextFacade {
    private final UserService userService;
    private final AuthService authService;
    private final MainService mainService;
    private final NotifyService notifyService;

    public BotContextFacade(UserService userService, AuthService authService, MainService mainService, NotifyService notifyService) {
        this.userService = userService;
        this.authService = authService;
        this.mainService = mainService;
        this.notifyService = notifyService;
    }

    public void authProcess(String userId) {
        authService.sendOAuthOffer(userId);
    }

    public void handleCommand(String userId, String command) {
        switch(command) {
            // рестарт бота, так как бд нема
            case RESTART: {
                userService.clear();
                break;
            }
            case START:
            case HELP: {
                mainService.sendAllCommands(userId);
                break;
            }
            case ABOUT: {
                mainService.tellAboutBot(userId);
                break;
            }
            case NOTIFY: {
                notifyService.offerNotifyTime(userId);
                break;
            }
            default:
                break;
        }
    }
}
