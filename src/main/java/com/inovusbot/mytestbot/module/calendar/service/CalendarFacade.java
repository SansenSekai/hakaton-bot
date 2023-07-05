package com.inovusbot.mytestbot.module.calendar.service;

import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class CalendarFacade {
    private final CalendarService calendarService;
    private final UserService userService;

    public CalendarFacade(CalendarService calendarService, UserService userService) {
        this.calendarService = calendarService;
        this.userService = userService;
    }

    public void showMenu(String userId) {
        calendarService.showMenu(userId);
    }

    public void handleCommand(String userId, String command) {
        String context = userService.getContext(userId);
        switch (context) {
            case "calendar-main": {
                if(command.equals("calendar-coworking")) {
                    calendarService.selectCoworking(userId);
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
