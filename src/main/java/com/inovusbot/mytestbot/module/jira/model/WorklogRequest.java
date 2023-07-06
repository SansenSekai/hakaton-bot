package com.inovusbot.mytestbot.module.jira.model;

import lombok.Data;

import java.util.List;

@Data
public class WorklogRequest {
    private String user;
    private List<IssueWorklog> issues;
}
