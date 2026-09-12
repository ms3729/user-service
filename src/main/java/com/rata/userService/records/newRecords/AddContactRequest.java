package com.rata.userService.records.newRecords;

import jakarta.validation.constraints.NotBlank;

public record AddContactRequest(
        @NotBlank String contactType,
        @NotBlank String value,
        boolean isPrimary,
        boolean verified
) {
}