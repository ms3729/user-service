package com.rata.userService.records.sms;

public record SmsTemplateRecord(String mobile, int templateId, SmsNameValueRecord... parameters) {
}
