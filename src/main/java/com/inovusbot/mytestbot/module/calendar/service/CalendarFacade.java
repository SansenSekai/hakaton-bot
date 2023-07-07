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
                    //calendarService.selectDay(userId);
                    calendarService.selectDay(userId);
                } else if(command.equals("calendar-today-events")) {
                    calendarService.fetchMyEvents(userId);
                }
                break;
            }
            case "calendar-coworking-day": {
                if(command.equals("calendar-tomorrow")) {
                    calendarService.selectWorkspace(userId);
                }
                break;
            }
            case "calendar-coworking-room": {
                String[] parts = command.split("-");
                String commandString = parts[1] + "-" + parts[2];
                Integer room = Integer.valueOf(parts[3]);
                if(commandString.equals("tomorrow-room")) {
                    calendarService.createCoworking(userId, room);
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
