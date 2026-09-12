package com.rata.userService.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserRequestDTO implements Serializable {
    private Long id;
    private String firstname;
    private String lastname;
    private String nationalCode;
    private String mobile;
    private String address;
    private String latitude;
    private String longitude;
    private String phoneNumber;
    private Boolean gender;
    private String birthDate;
    private boolean enabled = true;
    private String email;
    private String avatar;
    private String type = "STAFF";
    private Integer countryId,stateId, cityId;
    private List<Long> rolesId;
    private String bankCardNumber;
    private String bankAccountNumber;
    private String iban;
    private Integer bankId;
    private String bankName;
    private String bankAccountOwner;
    private String fatherName;
    private String postalCode;
    private String PersonnelCode;
    private Integer detailedCode;
    private List<Long> units;
    private String lang = "fa";

}
