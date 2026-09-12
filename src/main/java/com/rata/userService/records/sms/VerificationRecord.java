package com.rata.userService.records.sms;

import java.util.Date;


public record VerificationRecord(String identifyCode, String mobile, long userId,
                                 Date sendDate, String code, String type, String data) {

}
