package com.rata.userService.services.interfaces.query;

import com.rata.userService.models.Verification;
import com.rata.userService.services.interfaces.BasicQueryService;

public interface VerificationQueryService extends BasicQueryService<Verification, Long> {

    boolean checkVerificationCode(String identifierCode, String smsCode, String data);

    String checkValidityResendSms(String data);
}
