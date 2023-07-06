package com.inovusbot.mytestbot.module.lunch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MenuItem {
    private String id;
    private String name;
    private String price;
}
