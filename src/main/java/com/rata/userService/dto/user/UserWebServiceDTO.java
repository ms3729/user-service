package com.rata.userService.dto.user;

import com.rata.userService.dto.PersonDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWebServiceDTO extends PersonDTO {
    private String birthDate;
    private String avatar;
    private String bankCardNumber;
    private String bankAccountNumber;
    private String iban;
    private Integer bankId;
    private String bankAccountOwner;
    private String personnelCode;
}
