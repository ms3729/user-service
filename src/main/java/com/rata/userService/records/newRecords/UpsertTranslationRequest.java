package com.rata.userService.records.newRecords;

import jakarta.validation.constraints.NotBlank;

public record UpsertTranslationRequest(
        @NotBlank String lang,
        String firstName,
        String lastName,
        String fatherName
) {
}