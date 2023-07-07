package com.inovusbot.mytestbot.module.notification.controller;

import com.inovusbot.mytestbot.module.notification.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeNotificationController {
    private final NotificationService notificationService;

    public FakeNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/log")
    public void pushWorklogsNotification(@RequestParam String userId) {
        notificationService.pushWorklogNotification(userId);
    }

    @GetMapping("/lunch")
    public void pushLunchNotification(@RequestParam String userId) {
        notificationService.pushLunchNotification(userId);
    }
    @GetMapping("/meet")
    public void pushMeetupNotification(@RequestParam String userId) {
        notificationService.pushMeetupNotification(userId);
    }
}
