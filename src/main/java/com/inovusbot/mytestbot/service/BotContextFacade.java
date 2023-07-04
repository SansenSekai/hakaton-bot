package com.inovusbot.mytestbot.service;

import com.inovusbot.mytestbot.module.auth.service.AuthService;
import com.inovusbot.mytestbot.module.main.service.MainService;
import com.inovusbot.mytestbot.module.notification.NotificationFacade;
import com.inovusbot.mytestbot.module.poker.service.PokerFacade;
import org.springframework.stereotype.Service;

import static com.inovusbot.mytestbot.config.Commands.*;

@Service
public class BotContextFacade {
    private final UserService userService;
    private final AuthService authService;
    private final MainService mainService;
    private final NotificationFacade notificationFacade;
    private final PokerFacade pokerFacade;

    public BotContextFacade(UserService userService, AuthService authService, MainService mainService, NotificationFacade notificationFacade, PokerFacade pokerFacade) {
        this.userService = userService;
        this.authService = authService;
        this.mainService = mainService;
        this.notificationFacade = notificationFacade;
        this.pokerFacade = pokerFacade;
    }

    public void authProcess(String userId) {
        authService.sendOAuthOffer(userId);
    }

    public void handleCommand(String userId, String command) {
        // переход по главному меню
        if(command.startsWith("/")) {
            switch(command) {
                // очистка бота, так как бд нема
                case "/" + RESTART: {
                    userService.clear();
                    break;
                }
                case "/" + START: {
                    mainService.gotoMainMenu(userId);
                    break;
                }
                case "/" + HELP: {
                    mainService.sendAllCommands(userId);
                    break;
                }
                case "/" + ABOUT: {
                    mainService.tellAboutBot(userId);
                    break;
                }
                case "/" + NOTIFICATIONS: {
                    notificationFacade.showMenu(userId);
                    break;
                }
                case "/" + POKER: {
                    pokerFacade.showMenu(userId);
                    break;
                }
                case "/" : {
                    mainService.gotoMainMenu(userId);
                }
                default:
                    mainService.handleErrorCommand(userId, command);
                    break;
            }
        } else { // или подменю
            String context = userService.getContext(userId);
            context = context.substring(0, context.indexOf("-"));
            switch(context) {
                // очистка бота, так как бд нема
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
                case "notification": {
                    notificationFacade.commandHandler(userId, command);
                    break;
                }
                default:
                    mainService.handleErrorCommand(userId, command);
                    break;
            }
        }
    }
}
