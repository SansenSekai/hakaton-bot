package com.inovusbot.mytestbot.module.poker.service;

import org.springframework.stereotype.Service;

@Service
public class PokerFacade {
    private final PokerService pokerService;

    public PokerFacade(PokerService pokerService) {
        this.pokerService = pokerService;
    }

    public void showMenu(String userId) {
        pokerService.showMenu(userId);
    }
}
