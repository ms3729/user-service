package com.rata.userService.records.sms;

public record VerifySmsTemplateRecord(String mobile, int templateId, SmsNameValueRecord... parameters) {
}
