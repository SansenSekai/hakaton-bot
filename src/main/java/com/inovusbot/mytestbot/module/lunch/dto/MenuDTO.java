package com.inovusbot.mytestbot.module.lunch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MenuDTO {
    private String date;
    private List<MenuItem> menuItems = new ArrayList<>();
}
