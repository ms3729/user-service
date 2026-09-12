package com.rata.userService.records.newRecords;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.Set;

public record UpdateEmployeeRequest(
        String defaultLanguage,

        @Valid
        Set<UpsertTranslationRequest> translations,

        LocalDate birthDate,
        Boolean gender,
        String nationalCode,
        String educationLevel,
        String personnelCode,
        Integer detailedCode,
        String avatarUrl
) {
}