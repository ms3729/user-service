package com.rata.userService.dto.user;

import lombok.Data;

@Data
public class UserFilter {
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String mobile;
    private String email;
}
