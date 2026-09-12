package com.rata.userService.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserTranslationDTO {

    private Long id;
    private String lang = "fa";
    private String firstname;
    private String lastname;
    private String fatherName;
    private String address;
    private Integer cityId;
    private Integer stateId;
    private Integer countryId;
    private String latitude;
    private String longitude;
    private String customerAgentName;

    public UserTranslationDTO() {
    }

    public UserTranslationDTO(Long id, String lang, String firstname, String lastname, String fatherName) {
        this.id = id;
        this.lang = lang;
        this.firstname = firstname;
        this.lastname = lastname;
        this.fatherName = fatherName;
    }

    public UserTranslationDTO(Long id, String lang, String firstname, String lastname, String fatherName,
                              String address, Integer cityId, Integer stateId, Integer countryId,
                              String latitude, String longitude,
                              String customerAgentName) {
        this(id, lang, firstname, lastname, fatherName);
        this.address = address;
        this.cityId = cityId;
        this.stateId = stateId;
        this.countryId = countryId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerAgentName = customerAgentName;
    }
}
