package com.rata.userService.records.newRecords;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * پیام مشترک برای ایجاد/به‌روزرسانی Party از سرویس‌های دیگر
 */
public record PartyUpsertMessage(
        UUID messageId,
        Integer schemaVersion,
        Instant occurredAt,
        String sourceService,
        String sourceEntityType,
        String sourceEntityId,
        Long organizationId,
        List<String> relationTypes,
        String partyType,
        String defaultLanguage,
        List<IdentifierDto> identifiers,
        Map<String, PersonNameDto> personTranslations,
        Map<String, OrganizationNameDto> organizationTranslations,
        ContactDto contacts,
        String username,
        String password,
        String nationalCode,
        String mobile
) {


        public record IdentifierDto(
                String type,
                String countryCode,
                String value
        ) {
        }

        public record PersonNameDto(
                String firstName,
                String lastName
        ) {
        }

        public record OrganizationNameDto(
                String legalName,
                String tradeName,
                String registrationName
        ) {
        }

        public record ContactDto(
                String mobile,
                String phone,
                String email,
                String website
        ) {
        }
}