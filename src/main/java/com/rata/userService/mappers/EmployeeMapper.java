package com.rata.userService.mappers;

import com.rata.userService.enums.PartyType;
import com.rata.userService.models.party.*;
import com.rata.userService.records.dto.ContactDto;
import com.rata.userService.records.dto.IdentifierDto;
import com.rata.userService.records.dto.MembershipDto;
import com.rata.userService.records.dto.TranslationDto;
import com.rata.userService.records.newRecords.EmployeeResponse;
import com.rata.userService.repositories.mysql.PartyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EmployeeMapper {

    private final PartyRepository partyRepository;

    public EmployeeResponse toResponse(Party party) {
        Person profile = party.getPerson();

        List<TranslationDto> translations = party.getPerson() != null
                ? party.getPersonTranslations().stream()
                .map(this::toTranslationDto)
                .toList()
                : List.of();

        List<IdentifierDto> identifiers = party.getIdentifiers().stream()
                .map(this::toIdentifierDto)
                .toList();

        List<ContactDto> contacts = party.getContacts().stream()
                .map(this::toContactDto)
                .toList();

        List<MembershipDto> memberships = buildMembershipDtos(party.getMemberships());

        return new EmployeeResponse(
                party.getId(),
                party.getPartyType().name(),
                party.getDefaultLanguage(),
                party.getDisplayName(),
                party.getPersonnelCode(),
                party.getDetailedCode(),
                party.getAvatarUrl(),
                profile != null ? profile.getBirthDate() : null,
                profile != null && profile.getGender() != null ? profile.getGender() : null,
                profile != null ? party.getNationalCode() : null,
                profile != null && profile.getEducationLevel() != null
                        ? profile.getEducationLevel().name() : null,
                translations,
                identifiers,
                contacts,
                memberships, null
        );
    }

    private TranslationDto toTranslationDto(PersonTranslation translation) {
        return new TranslationDto(
                translation.getLang(),
                translation.getFirstname(),
                translation.getLastname()
        );
    }

    private IdentifierDto toIdentifierDto(PartyIdentifier identifier) {
        return new IdentifierDto(
                identifier.getId(),
                identifier.getIdentifierType().name(),
                identifier.getValue(),
                identifier.isVerified()
        );
    }

    private ContactDto toContactDto(PartyContact contact) {
        return new ContactDto(
                contact.getId(),
                contact.getContactType().name(),
                contact.getValue(),
                contact.isPrimary(),
                contact.isVerified()
        );
    }

    // ==================== ساخت MembershipDtoها ====================

    private List<MembershipDto> buildMembershipDtos(Set<Membership> memberships) {
        if (memberships == null || memberships.isEmpty()) {
            return List.of();
        }

        // استخراج همه organizationId ها
        Set<Long> organizationIds = memberships.stream()
                .map(m -> m.getOrganization().getPartyId())
                .collect(Collectors.toSet());

        // بارگذاری همه سازمان‌ها در یک query (جلوگیری از N+1)
        Map<Long, Party> organizationsMap = partyRepository.findAllById(organizationIds)
                .stream()
                .filter(p -> p.getPartyType() == PartyType.ORGANIZATION)
                .collect(Collectors.toMap(Party::getId, p -> p));

        // ساخت MembershipDto ها
        return memberships.stream()
                .map(m -> toMembershipDto(m, organizationsMap))
                .toList();
    }

    /**
     * تبدیل Membership به MembershipDto
     * نام سازمان از PartyRepository گرفته می‌شود (چون سازمان یک Party از نوع ORGANIZATION است)
     */
    private MembershipDto toMembershipDto(
            Membership membership,
            Map<Long, Party> organizationsMap
    ) {
        Party organization = organizationsMap.get(membership.getId());

        String organizationName = null;
        String organizationDefaultLanguage = null;

        if (organization != null) {
            // نام نمایشی سازمان با زبان پیش‌فرض خودش
            organizationName = organization.getDisplayName();
            organizationDefaultLanguage = organization.getDefaultLanguage();

            // اگر displayName null بود، از اولین ترجمه موجود استفاده کن
            if (organizationName == null && organization.getOrganizationTranslations() != null
                    && !organization.getOrganizationTranslations().isEmpty()) {
                OrganizationTranslation firstTranslation = organization
                        .getOrganizationTranslations().iterator().next();
                organizationName = firstTranslation.getLegalName();
            }
        }

        return new MembershipDto(
                membership.getId(),
                membership.getOrganization().getPartyId(),
                organizationName,
                organizationDefaultLanguage,
                membership.getRelationType().name(),
                membership.isEnabled()
        );
    }
}