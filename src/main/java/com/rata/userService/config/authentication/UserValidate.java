package com.rata.userService.config.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserValidate {
    private String identifierCode;
    private String smsCode;
    private String username;
}
