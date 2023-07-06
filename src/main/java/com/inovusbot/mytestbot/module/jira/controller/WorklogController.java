package com.inovusbot.mytestbot.module.jira.controller;

import com.inovusbot.mytestbot.module.jira.client.dto.SearchByJqlResponse;
import com.inovusbot.mytestbot.module.jira.model.WorklogRequest;
import com.inovusbot.mytestbot.module.jira.service.WorklogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class WorklogController {

    private final WorklogsService worklogsService;

    @PostMapping("/worklogs")
    public void addWorklogs(@RequestBody WorklogRequest request) {
        worklogsService.addWorklogs(request.getIssues());
    }

    @GetMapping("/inprogress")
    public SearchByJqlResponse inProgress() {
        return worklogsService.getInProgress();
    }

}
