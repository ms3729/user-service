package com.rata.userService.records.dto;

public record IdentifierDto(
        long id,
        String identifierType,
        String value,
        boolean verified
) {
}