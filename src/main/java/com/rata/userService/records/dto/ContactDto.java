package com.rata.userService.records.dto;

public record ContactDto(
        long id,
        String contactType,
        String value,
        boolean isPrimary,
        boolean verified
) {
}