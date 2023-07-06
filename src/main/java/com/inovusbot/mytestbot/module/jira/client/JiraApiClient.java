package com.inovusbot.mytestbot.module.jira.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.inovusbot.mytestbot.module.jira.client.dto.AddJiraWorklogRequest;
import com.inovusbot.mytestbot.module.jira.client.dto.SearchByJqlRequest;
import com.inovusbot.mytestbot.module.jira.client.dto.SearchByJqlResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "JiraApiClient", url = "${jira.url}")
public interface JiraApiClient {

    @RequestMapping(method = RequestMethod.POST, value = "/rest/api/2/issue/{issue}/worklog", produces = "application/json")
    JsonNode addWorklog(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @PathVariable("issue") String issue,
            @RequestBody AddJiraWorklogRequest request
    );

    @RequestMapping(method = RequestMethod.POST, value = "/rest/api/2/search", produces = "application/json")
    SearchByJqlResponse search(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @RequestBody SearchByJqlRequest request
    );
}
