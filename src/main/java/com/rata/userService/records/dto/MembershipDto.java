package com.rata.userService.records.dto;

public record MembershipDto(
        long id,
        long organizationId,
        String legalName,
        String lang,
        String relationType,
        boolean enabled
) {
}