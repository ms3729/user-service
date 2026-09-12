package com.rata.userService.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String mobile;
    private String address;
    private Boolean gender;
    private String birthDate;
    private String email;
    private String avatar;
    private Integer countryId,stateId, cityId;
    private String fatherName;
    private String postalCode;
    private String personnelCode;
    private String lang = "fa";
    private String latitude;
    private String longitude;
    private String phoneNumber;

}
