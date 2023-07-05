package com.inovusbot.mytestbot.module.notification.service;

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

    public void handleCommand(String userId, String command) {
        String context = userService.getContext(userId);
        switch (context) {
            case "notifications-main": {
                if(command.equals("notifications-create")) {
                    notificationService.createNotificationMenu(userId);
                } else if(command.equals("notifications-delete")) {
                    notificationService.deleteNotificationMenu(userId);
                } else if(command.equals("notifications-pause")) {
                    notificationService.pauseNotifications(userId);
                }
                break;
            }
            case "notification-choose-time": {
                notificationService.setNotificationTime(userId, command);
                break;
            }
            default: {
                mainService.handleErrorCommand(userId, command);
            }
        }
    }

    public void showMenu(String userId) {
        notificationService.showMenu(userId);
    }
}
