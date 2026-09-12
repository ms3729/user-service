package com.rata.userService.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception برای پیام‌های نامعتبر دریافتی از صف
 * این پیام‌ها قابل پردازش نیستند و باید به DLQ منتقل شوند
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMessageException extends RuntimeException {

    public InvalidMessageException(String message) {
        super(message);
    }

    public InvalidMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidMessageException of(String message) {
        return new InvalidMessageException(message);
    }

    public static InvalidMessageException of(String message, Throwable cause) {
        return new InvalidMessageException(message, cause);
    }
}