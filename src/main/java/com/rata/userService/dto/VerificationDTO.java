package com.rata.userService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationDTO {
    private String type;
    private long userId;
    private String data;
    private String amount;
}
