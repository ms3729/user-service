package com.rata.userService.records.newRecords;

import jakarta.validation.constraints.NotBlank;

public record AddIdentifierRequest(
        @NotBlank String identifierType,
        @NotBlank String value,
        boolean verified
) {
}