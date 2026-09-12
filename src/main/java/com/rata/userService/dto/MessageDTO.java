package com.rata.userService.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO {
    private String dataType;
    private String level;
    private String channel;
    private long templateId;
    private Map<String, String> data;
    private String target;
    private String sender;
    private String type;
}