package com.inovusbot.mytestbot.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserState {
    private String chatId;
    private String username;
    private Boolean authorized;
    private String context;
    private Integer lastMessageId;

    private String googleToken;
}
