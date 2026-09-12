package com.rata.userService.dto;

import com.rata.userService.dto.user.UserTranslationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class PersonDTO {
    private Long id;
    private List<UserTranslationDTO> translations = new LinkedList<>();
    private String nationalCode;
    private String postalCode;
    private String mobile;
    private String phoneNumber;
    private Boolean gender;
    private String type;
    private String email;
    private String description;
    private Integer detailedCode;
    private String financialCode;
}
