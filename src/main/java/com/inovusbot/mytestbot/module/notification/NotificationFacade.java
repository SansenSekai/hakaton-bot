package com.inovusbot.mytestbot.module.notification;

import com.inovusbot.mytestbot.module.main.service.MainService;
import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class NotificationFacade {
    private final NotificationService notificationService;
    private final UserService userService;

    private final MainService mainService;

    public NotificationFacade(NotificationService notificationService, UserService userService, MainService mainService) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.mainService = mainService;
    }

    public void commandHandler(String userId, String command) {
        String context = userService.getContext(userId);
        switch (context) {
            case "notification-menu": {
                notificationService.goToSubMenu(userId, command);
                break;
            }
            case "notification-choose-time": {
                notificationService.setNotificationTime(userId, command);
                break;
            }
            case "notification-turn-on-off": {
                notificationService.turnOnOffNotification(userId, command);
                break;
            }
            default: {
                mainService.handleErrorCommand(userId, command);
            }
        }
    }
}
