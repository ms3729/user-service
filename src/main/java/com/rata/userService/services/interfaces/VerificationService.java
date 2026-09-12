package com.rata.userService.services.interfaces;

import com.rata.userService.dto.VerificationDTO;
import com.rata.userService.models.User;

public interface VerificationService{

    String save(VerificationDTO dto);

    String saveLogin(User user);

    boolean checkVerificationCode(String identifierCode, String smsCode, String data);

    String checkValidityResendSms(String data);

}
