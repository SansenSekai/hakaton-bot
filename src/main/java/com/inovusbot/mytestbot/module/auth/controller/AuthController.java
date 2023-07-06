package com.inovusbot.mytestbot.module.auth.controller;

import com.inovusbot.mytestbot.module.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("login")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public RedirectView getToken(@RequestParam(name = "code") String code, @RequestParam(name = "state") String userId) {
        System.out.println("Code: " + code);
        authService.updateUserOAuthCode(userId, code);
        authService.fetchAccessToken(userId);
        return new RedirectView("https://t.me/haka_2023_07_bot");
    }
}
