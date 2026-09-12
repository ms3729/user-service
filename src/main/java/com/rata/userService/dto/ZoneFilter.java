package com.rata.userService.dto;

import lombok.Getter;

@Getter
public class ZoneFilter {
    private int id;
    private String code;
    private final String lang;

    public ZoneFilter(int id, String lang) {
        this.id = id;
        this.lang = lang;
    }

    public ZoneFilter(String code, String lang) {
        this.code = code;
        this.lang = lang;
    }
}
