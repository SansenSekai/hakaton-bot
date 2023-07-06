package com.inovusbot.mytestbot.module.jira.client.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddJiraWorklogRequest {
    int timeSpentSeconds;
    String started;
    String comment;
}
