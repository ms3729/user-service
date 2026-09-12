package com.rata.userService.records.newRecords;

import com.rata.userService.models.User;
import com.rata.userService.records.dto.ContactDto;
import com.rata.userService.records.dto.IdentifierDto;
import com.rata.userService.records.dto.MembershipDto;
import com.rata.userService.records.dto.TranslationDto;

import java.time.LocalDate;
import java.util.List;

public record EmployeeResponse(
        long partyId,
        String partyType,
        String defaultLanguage,
        String displayName,
        String personnelCode,
        Integer detailedCode,
        String avatarUrl,

        // پروفایل شخص
        LocalDate birthDate,
        Boolean gender,
        String nationalCode,
        String educationLevel,
        List<TranslationDto> translations,
        List<IdentifierDto> identifiers,
        List<ContactDto> contacts,
        List<MembershipDto> memberships,
        UserInfo user
) {
    public EmployeeResponse withUser(User user) {
        UserInfo userInfo = user != null
                ? new UserInfo(
                user.getId(),
                user.getUsername()
        )
                : null;

        return new EmployeeResponse(
                partyId, partyType, defaultLanguage, displayName,
                personnelCode, detailedCode, avatarUrl, birthDate, gender, nationalCode,
                educationLevel, translations, identifiers, contacts,
                memberships, userInfo
        );
    }

    public record UserInfo(
            long id,
            String username
    ) {
    }
}