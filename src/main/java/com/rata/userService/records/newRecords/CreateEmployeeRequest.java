package com.rata.userService.records.newRecords;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record CreateEmployeeRequest(

        @NotBlank(message = "defaultLanguage الزامی است")
        String defaultLanguage,

        @NotNull(message = "حداقل یک ترجمه الزامی است")
        @Size(min = 1, message = "حداقل یک ترجمه الزامی است")
        @Valid
        Set<UpsertTranslationRequest> translations,

        @Valid
        Set<AddIdentifierRequest> identifiers,

        @Valid
        Set<AddContactRequest> contacts,

        @NotNull(message = "حداقل یک شرکت الزامی است")
        @Size(min = 1, message = "کارمند باید حداقل به یک شرکت متصل شود")
        List<AssignOrganizationRequest> organizations,

        // اطلاعات پروفایل شخص
        LocalDate birthDate,
        Boolean gender,
        String nationalCode,
        String educationLevel,
        String personnelCode,
        Integer detailedCode
) {
}