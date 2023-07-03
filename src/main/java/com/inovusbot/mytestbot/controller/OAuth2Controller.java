package com.inovusbot.mytestbot.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class OAuth2Controller {
    BotController botController;

    @Autowired
    public OAuth2Controller(BotController botController) {
        this.botController = botController;
    }

    @GetMapping
    public void getToken(@RequestParam(name = "code") String googleCode, @RequestParam(name = "state") String userId) {
        botController.updateUserOAuthCode(googleCode, userId);
    }
}
