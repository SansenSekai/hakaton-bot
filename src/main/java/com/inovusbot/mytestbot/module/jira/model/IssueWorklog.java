package com.inovusbot.mytestbot.module.jira.model;

import lombok.Data;

@Data
public class IssueWorklog {
    private String issueId;
    private int minutes;
}
