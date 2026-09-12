package com.rata.userService.config.aspect;

public record LogRecord(String clientIp, String service, String className, String method,String clientInfo) {
}
