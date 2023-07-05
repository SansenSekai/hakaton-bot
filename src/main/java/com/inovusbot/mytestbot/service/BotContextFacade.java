package com.inovusbot.mytestbot.service;

import com.inovusbot.mytestbot.module.auth.service.AuthService;
import com.inovusbot.mytestbot.module.calendar.service.CalendarFacade;
import com.inovusbot.mytestbot.module.lunch.service.LunchFacade;
import com.inovusbot.mytestbot.module.main.service.MainService;
import com.inovusbot.mytestbot.module.notification.service.NotificationFacade;
import com.inovusbot.mytestbot.module.poker.service.PokerFacade;
import org.springframework.stereotype.Service;

@Service
public class BotContextFacade {
    private final UserService userService;
    private final AuthService authService;
    private final MainService mainService;
    private final NotificationFacade notificationFacade;
    private final PokerFacade pokerFacade;
    private final LunchFacade lunchFacade;
    private final CalendarFacade calendarFacade;

    public BotContextFacade(UserService userService, AuthService authService, MainService mainService, NotificationFacade notificationFacade, PokerFacade pokerFacade, LunchFacade lunchFacade, CalendarFacade calendarFacade) {
        this.userService = userService;
        this.authService = authService;
        this.mainService = mainService;
        this.notificationFacade = notificationFacade;
        this.pokerFacade = pokerFacade;
        this.lunchFacade = lunchFacade;
        this.calendarFacade = calendarFacade;
    }

    public void authProcess(String userId) {
        authService.sendOAuthOffer(userId);
    }

    public void handleCommand(String userId, String command) {
        // переход по главному меню
        if(command.startsWith("/")) {
            switch(command) {
                // очистка бота, так как бд нема
                case "/restart": {
                    userService.clear();
                    break;
                }
                case "/start": {
                    mainService.firstMessage(userId);
                    break;
                }
                case "/" :
                case "/menu": {
                    mainService.gotoMainMenu(userId);
                    break;
                }
                case "/help": {
                    mainService.sendAllCommands(userId);
                    break;
                }
                case "/about": {
                    mainService.tellAboutBot(userId);
                    break;
                }
                case "/calendar": {
                    calendarFacade.showMenu(userId);
                    break;
                }
                case "/notifications": {
                    notificationFacade.showMenu(userId);
                    break;
                }
                case "/lunch": {
                    lunchFacade.showMenu(userId);
                    break;
                }
                case "/poker": {
                    pokerFacade.showMenu(userId);
                    break;
                }
                default:
                    mainService.handleErrorCommand(userId, command);
                    break;
            }
        } else { // или подменю
            String context = userService.getContext(userId);
            context = context.substring(0, context.indexOf("-"));
            switch(context) {
                case "notifications": {
                    notificationFacade.handleCommand(userId, command);
                    break;
                }
                case "poker": {
                    pokerFacade.handleCommand(userId, command);
                    break;
                }
                case "calendar": {
                    calendarFacade.handleCommand(userId, command);
                    break;
                }
                default:
                    mainService.handleErrorCommand(userId, command);
                    break;
            }
        }
    }
}
