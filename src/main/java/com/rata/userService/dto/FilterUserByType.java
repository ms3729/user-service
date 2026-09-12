package com.rata.userService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterUserByType {
    @JsonProperty("type")
    private String type;
    @JsonProperty("ides")
    private List<Long> ides;

    public FilterUserByType() {
    }
}
