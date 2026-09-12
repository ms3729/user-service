package com.rata.userService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private long person;
    private String lang = "fa";
    private String postalCode;
    private String address;
    private Integer cityId;
}
