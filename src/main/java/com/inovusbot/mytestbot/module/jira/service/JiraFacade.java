package com.inovusbot.mytestbot.module.jira.service;

import com.inovusbot.mytestbot.module.main.service.MainService;
import com.inovusbot.mytestbot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class JiraFacade {
    private final JiraService jiraService;
    private final UserService userService;

    private final MainService mainService;

    public JiraFacade(JiraService jiraService, UserService userService, MainService mainService) {
        this.jiraService = jiraService;
        this.userService = userService;
        this.mainService = mainService;
    }

    public void handleCommand(String userId, String command) {
        String context = userService.getContext(userId);
        switch (context) {
            case "jira-main": {
                if(command.equals("jira-worklogs")) {
                    jiraService.showAllTasks(userId);
                } else if(command.equals("jira-status")) {
                    jiraService.statusMenu(userId);
                }
                break;
            }
            case "jira-worklogs-time":
            case "jira-worklogs": {
                String[] parts = command.split("-");
                String issue = parts[3] + "-" + parts[4];
                Integer hours = Integer.valueOf(parts[5]);
                Integer minutes = Integer.valueOf(parts[6]);
                if(parts[2].equals("time")) {
                    jiraService.setWorklogTime(userId, issue, hours, minutes);
                } else if(parts[2].equals("confirm")) {
                    jiraService.confirmWorklog(userId, issue, hours, minutes);
                }
                break;
            }
            case "jira-status": {
                if(command.equals("jira-status-delete-all")) {
                    jiraService.statusDone(userId);
                }
                break;
            }
            default: {
                mainService.handleErrorCommand(userId, command);
            }
        }
    }

    public void handlePush(String userId, String command) {
        if(command.equals("push-jira-worklogs")) {
            jiraService.showAllTasks(userId);
        } else {
            String[] parts = command.split("-");
            String issue = parts[4] + "-" + parts[5];
            Integer hours = Integer.valueOf(parts[6]);
            Integer minutes = Integer.valueOf(parts[7]);
            System.out.println(command);
            if (parts[3].equals("custom")) {
                jiraService.setWorklogTime(userId, issue, hours, minutes);
            } else if (parts[3].equals("default")) {
                jiraService.confirmWorklog(userId, issue, hours, minutes);
            }
        }
    }

    public void showMenu(String userId) {
        jiraService.showMenu(userId);
    }
}
