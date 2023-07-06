package com.inovusbot.mytestbot.module.jira.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchByJqlIssueItem {
    private String key;
    private SearchByJqlIssueItemFields fields;
}
