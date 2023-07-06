package com.inovusbot.mytestbot.module.jira.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue {
    private String label;
    private String summary;
}
