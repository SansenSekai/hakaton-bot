package com.inovusbot.mytestbot.module.jira.service;

import com.inovusbot.mytestbot.module.jira.client.JiraApiClient;
import com.inovusbot.mytestbot.module.jira.client.dto.AddJiraWorklogRequest;
import com.inovusbot.mytestbot.module.jira.client.dto.SearchByJqlRequest;
import com.inovusbot.mytestbot.module.jira.client.dto.SearchByJqlResponse;
import com.inovusbot.mytestbot.module.jira.model.IssueWorklog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorklogsService {

    private final JiraApiClient jiraApiClient;

    public void addWorklogs(List<IssueWorklog> issues) {
        for (IssueWorklog issue : issues) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            AddJiraWorklogRequest addJiraWorklogRequest = AddJiraWorklogRequest.builder()
                    .started(formatter.format(OffsetDateTime.now()))
                    .comment("Added with Worklog bot")
                    .timeSpentSeconds(issue.getMinutes() * 60)
                    .build();
            String authorizationHeader = "Basic dml2YW5vdkBpLW5vdnVzLnJ1OkFUQVRUM3hGZkdGMHJaTjZWMEJNeUpDb2FWN0ZuMllwQ3NFZS1jNHRwdVB6T2Z2RDJFeDlKd0FPR1YzWVJ3VXFFelZKblNCQ0htR0tDSVg2R01uMjhXSjFXa0tPaXlqMzdHaWNCWXFCa19LRGI2MTBWa0ttVGZpSjFpLUtHaEp5Mk1NZEF6Y2NLNWxfY0tGVjdQd2FmME43Vm9fdjBlT2dfYWx5VFJRYmxTcFo2eGtpaEgzQzBTaz0wM0VBODZFQg==";
            jiraApiClient.addWorklog(authorizationHeader, issue.getIssueId(), addJiraWorklogRequest);
        }
    }

    public SearchByJqlResponse getInProgress() {
        SearchByJqlRequest searchInProgressRequest = SearchByJqlRequest.builder()
                .jql("assignee in (currentUser()) AND created >= -30d AND status = \"In Progress\"")
                .build();
        String authorizationHeader = "Basic dml2YW5vdkBpLW5vdnVzLnJ1OkFUQVRUM3hGZkdGMHJaTjZWMEJNeUpDb2FWN0ZuMllwQ3NFZS1jNHRwdVB6T2Z2RDJFeDlKd0FPR1YzWVJ3VXFFelZKblNCQ0htR0tDSVg2R01uMjhXSjFXa0tPaXlqMzdHaWNCWXFCa19LRGI2MTBWa0ttVGZpSjFpLUtHaEp5Mk1NZEF6Y2NLNWxfY0tGVjdQd2FmME43Vm9fdjBlT2dfYWx5VFJRYmxTcFo2eGtpaEgzQzBTaz0wM0VBODZFQg==";
        return jiraApiClient.search(authorizationHeader, searchInProgressRequest);
    }
}
