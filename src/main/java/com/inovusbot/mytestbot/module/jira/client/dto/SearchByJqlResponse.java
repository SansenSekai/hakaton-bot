package com.inovusbot.mytestbot.module.jira.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchByJqlResponse {
    private List<SearchByJqlIssueItem> issues;
}
