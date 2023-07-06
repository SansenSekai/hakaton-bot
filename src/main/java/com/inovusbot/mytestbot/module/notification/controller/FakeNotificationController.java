package com.inovusbot.mytestbot.module.notification.controller;

import com.inovusbot.mytestbot.module.notification.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeNotificationController {
    private final NotificationService notificationService;

    public FakeNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/log")
    public void pushWorklogsNotification() {
        notificationService.pushWorklogNotification();
    }

    @GetMapping("/lunch")
    public void pushLunchNotification() {
        notificationService.pushLunchNotification();
    }
    @GetMapping("/meet")
    public void pushMeetupNotification() {
        notificationService.pushMeetupNotification();
    }
}
