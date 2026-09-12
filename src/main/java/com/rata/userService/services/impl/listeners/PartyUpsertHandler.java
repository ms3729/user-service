package com.rata.userService.services.impl.listeners;

import com.rata.userService.enums.ContactType;
import com.rata.userService.enums.IdentifierType;
import com.rata.userService.enums.PartyType;
import com.rata.userService.enums.RelationType;
import com.rata.userService.errorHandling.BusinessException;
import com.rata.userService.errorHandling.InvalidMessageException;
import com.rata.userService.models.party.*;
import com.rata.userService.records.newRecords.PartyUpsertMessage;
import com.rata.userService.repositories.mysql.*;
import com.rata.userService.services.interfaces.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PartyUpsertHandler {


    private final OrganizationTranslationRepository organizationTranslationRepository;
    private final PartyIdentifierRepository partyIdentifierRepository;
    private final PartyContactRepository partyContactRepository;
    private final MembershipRepository membershipRepository;
    private final PartyExternalRefRepository partyExternalRefRepository;
    private final UserService userService;
    private final PersonTranslationService personTranslationService;
    private final PersonService personService;
    private final OrganizationService organizationService;
    private final PartyService partyService;


    @Transactional
    public Party handle(PartyUpsertMessage message) {
        // ۱. اعتبارسنجی پیام
        validate(message);

        // ۲. پیدا کردن یا ساختن Party
        Party party = findOrCreateParty(message);

        // ۳. ذخیره External Ref (برای ردیابی)
        saveExternalRef(party, message);

        // ۴. مدیریت Membership ها (با چک کردن دقیق سناریوها)
        upsertMemberships(party, message);

        // ۵. به‌روزرسانی  (Person/Organization)
        upsert(party, message);

        // ۶. به‌روزرسانی ترجمه‌ها
        upsertTranslations(party, message);

        // ۷. به‌روزرسانی شناسه‌ها
        upsertIdentifiers(party, message);

        // ۸. به‌روزرسانی تماس‌ها
        upsertContacts(party, message);

        // ۹. ساخت User برای لاگین (اگر لازم باشد)
        createLoginUserIfNeeded(party, message);

        return party;
    }

    // ==================== findOrCreateParty ====================

    /**
     * پیدا کردن Party موجود یا ساختن Party جدید
     * اولویت‌ها:
     *  1. External Ref (sourceService + sourceEntityId)
     *  2. Identifier (NATIONAL_ID, COMPANY_REG, ...)
     *  3. ساخت Party جدید
     */
    private Party findOrCreateParty(PartyUpsertMessage message) {
        // ۱. بررسی External Ref
        Party party = findByExternalRef(message);
        if (party != null) {
            log.debug("Party از External Ref پیدا شد: partyId={}", party.getId());
            return party;
        }

        // ۲. بررسی Identifiers
        party = findByIdentifiers(message);
        if (party != null) {
            log.debug("Party از Identifier پیدا شد: partyId={}", party.getId());
            return party;
        }

        // ۳. ساخت Party جدید
        party = partyService.save(message);
        log.info("Party جدید ساخته شد: partyId={}, type={}", party.getId(), party.getPartyType());
        return party;
    }

    private Party findByExternalRef(PartyUpsertMessage message) {
        return partyExternalRefRepository
                .findBySourceServiceAndSourceEntityId(
                        message.sourceService(),
                        message.sourceEntityId()
                )
                .map(ref -> partyService.findById(ref.getParty().getId()).orElse(null))
                .orElse(null);
    }

    private Party findByIdentifiers(PartyUpsertMessage message) {
        if (message.identifiers() == null || message.identifiers().isEmpty()) {
            return null;
        }

        // اولویت: NATIONAL_ID, COMPANY_REG
        for (PartyUpsertMessage.IdentifierDto identifier : message.identifiers()) {
            if (identifier.value() == null || identifier.value().isBlank()) {
                continue;
            }

            IdentifierType identifierType = parseIdentifierType(identifier.type());

            Optional<PartyIdentifier> found = partyIdentifierRepository
                    .findByIdentifierTypeAndValue(identifierType, identifier.value());

            if (found.isPresent()) {
                return found.get().getParty();
            }
        }

        return null;
    }

    // ==================== upsertMemberships (با سناریوهای کامل) ====================

    /**
     * مدیریت Membership ها با پوشش سناریوهای زیر:
     *
     * ۱. Membership با همین organizationId و relationType موجود و ACTIVE است → رد (idempotent)
     * ۲. Membership با همین organizationId و relationType موجود ولی INACTIVE است → فعال‌سازی مجدد
     * ۳. Membership با همین organizationId ولی relationType متفاوت → Membership جدید اضافه شود
     * ۴. Membership با این organizationId وجود ندارد → Membership جدید ساخته شود
     */
    private void upsertMemberships(Party party, PartyUpsertMessage message) {
        if (message.organizationId() == null || message.relationTypes() == null) {
            log.debug("organizationId یا relationTypes در پیام وجود ندارد، membership ساخته نمی‌شود");
            return;
        }

        for (String relationTypeStr : message.relationTypes()) {
            if (relationTypeStr == null || relationTypeStr.isBlank()) {
                continue;
            }

            RelationType relationType = parseRelationType(relationTypeStr);

            // چک ۱: آیا Membership با همین organizationId و relationType وجود دارد؟
            Optional<Membership> existingExact = membershipRepository
                    .findByPartyIdAndOrganizationPartyIdAndRelationType(
                            party.getId(), message.organizationId(), relationType);

            if (existingExact.isPresent()) {
                Membership membership = existingExact.get();

                // اگر INACTIVE است، فعالش کن
                if (!membership.isEnabled()) {
                    log.info("Membership غیرفعال partyId={} در شرکت {} با relationType={} فعال می‌شود",
                            party.getId(), message.organizationId(), relationType);
                    membership.setEnabled(true);
                    membershipRepository.save(membership);
                } else {
                    log.debug("Membership تکراری (idempotent): partyId={}, organizationId={}, relationType={}",
                            party.getId(), message.organizationId(), relationType);
                }
                continue;
            }

            // چک ۲: آیا Membership دیگری در همین شرکت وجود دارد؟
            List<Membership> existingInOrganization = membershipRepository
                    .findAllByPartyIdAndOrganizationPartyId(party.getId(), message.organizationId());

            if (!existingInOrganization.isEmpty()) {
                List<String> existingTypes = existingInOrganization.stream()
                        .map(m -> m.getRelationType().name() + "(" + m.isEnabled() + ")")
                        .toList();

                log.info(
                        "Party {} قبلاً در شرکت {} membership با RelationType های {} دارد. " +
                                "Membership جدید با RelationType {} اضافه می‌شود (چند نقشی)",
                        party.getId(),
                        message.organizationId(),
                        existingTypes,
                        relationType
                );
            }
            Optional<Organization> organization = organizationService.findById(message.organizationId());
            if (organization.isPresent()) {
                // چک ۳: ساخت Membership جدید
                Membership newMembership = Membership.builder()
                        .party(party)
                        .organization(organization.get())
                        .relationType(relationType)
                        .build();

                membershipRepository.save(newMembership);

                log.info("Membership جدید ساخته شد: partyId={}, organizationId={}, relationType={}",
                        party.getId(), message.organizationId(), relationType);
            }
        }
    }

    // ==================== upsert ====================

    /**
     * اگر  (Person/Organization) وجود ندارد، آن را می‌سازد.
     */
    private void upsert(Party party, PartyUpsertMessage message) {
        if (party.getPartyType() == PartyType.PERSON) {
            upsertPerson(party);
        } else {
            upsertOrganization(party);
        }
    }

    private void upsertPerson(Party party) {
        boolean exists = personService.existsById(party.getId());
        if (!exists) {
            personService.save(party);
        }
    }

    private void upsertOrganization(Party party) {
        boolean exists = organizationService.existsById(party.getId());
        if (!exists) {
            organizationService.save(party);
        }
    }

    // ==================== بقیه متدها (همان قبلی) ====================

    private void upsertTranslations(Party party, PartyUpsertMessage message) {
        if (party.getPartyType() == PartyType.PERSON) {
            upsertPersonTranslations(party, message);
        } else {
            upsertOrganizationTranslations(party, message);
        }
    }

    private void upsertPersonTranslations(Party party, PartyUpsertMessage message) {
        if (message.personTranslations() == null) {
            return;
        }
        personTranslationService.save(party, message);
    }

    private void upsertOrganizationTranslations(Party party, PartyUpsertMessage message) {
        if (message.organizationTranslations() == null) {
            return;
        }

        for (Map.Entry<String, PartyUpsertMessage.OrganizationNameDto> entry :
                message.organizationTranslations().entrySet()) {

            String languageCode = entry.getKey();
            PartyUpsertMessage.OrganizationNameDto dto = entry.getValue();

            OrganizationTranslation translation = organizationTranslationRepository
                    .findByPartyIdAndLang(party.getId(), languageCode)
                    .orElseGet(() -> {
                        OrganizationTranslation newTranslation = new OrganizationTranslation();
                        newTranslation.setParty(party);
                        newTranslation.setLang(languageCode);
                        return newTranslation;
                    });

            translation.setLegalName(dto.legalName());
            translation.setTradeName(dto.tradeName());
            translation.setRegistrationName(dto.registrationName());

            organizationTranslationRepository.save(translation);
        }
    }

    private void upsertIdentifiers(Party party, PartyUpsertMessage message) {
        if (message.identifiers() == null || message.identifiers().isEmpty()) {
            return;
        }

        for (PartyUpsertMessage.IdentifierDto identifier : message.identifiers()) {
            if (identifier.value() == null || identifier.value().isBlank()) {
                continue;
            }

            IdentifierType identifierType = parseIdentifierType(identifier.type());

            boolean exists = partyIdentifierRepository
                    .existsByPartyIdAndIdentifierTypeAndValue(
                            party.getId(), identifierType, identifier.value());

            if (exists) {
                continue;
            }

            PartyIdentifier partyIdentifier = PartyIdentifier.builder()
                    .party(party)
                    .identifierType(identifierType)
                    .value(identifier.value())
                    .verified(false)
                    .build();

            partyIdentifierRepository.save(partyIdentifier);
        }
    }

    private void upsertContacts(Party party, PartyUpsertMessage message) {
        if (message.contacts() == null) {
            return;
        }

        PartyUpsertMessage.ContactDto contacts = message.contacts();

        if (contacts.mobile() != null && !contacts.mobile().isBlank()) {
            saveContact(party, ContactType.MOBILE, contacts.mobile(), true);
        }
        if (contacts.phone() != null && !contacts.phone().isBlank()) {
            saveContact(party, ContactType.PHONE, contacts.phone(), false);
        }
        if (contacts.email() != null && !contacts.email().isBlank()) {
            saveContact(party, ContactType.EMAIL, contacts.email(), false);
        }
        if (contacts.website() != null && !contacts.website().isBlank()) {
            saveContact(party, ContactType.WEBSITE, contacts.website(), false);
        }
    }

    private void saveContact(Party party, ContactType contactType, String value, boolean isPrimary) {
        boolean exists = partyContactRepository
                .existsByPartyIdAndContactTypeAndValue(party.getId(), contactType, value);

        if (exists) {
            return;
        }

        PartyContact contact = PartyContact.builder()
                .party(party)
                .contactType(contactType)
                .value(value)
                .isPrimary(isPrimary)
                .verified(false)
                .build();

        partyContactRepository.save(contact);
    }

    private void createLoginUserIfNeeded(Party party, PartyUpsertMessage message) {
        boolean userExists = userService.userExistsForParty(party.getId());
        if (userExists) {
            return;
        }
        userService.createUserForParty(party);
        log.info("User برای partyId={} ساخته شد: username={}", party.getId(), party.getNationalCode());
    }

    private void saveExternalRef(Party party, PartyUpsertMessage message) {
        boolean exists = partyExternalRefRepository
                .findBySourceServiceAndSourceEntityId(
                        message.sourceService(),
                        message.sourceEntityId()
                )
                .isPresent();

        if (exists) {
            return;
        }

        PartyExternalRef externalRef = PartyExternalRef.builder()
                .party(party)
                .sourceService(message.sourceService())
                .sourceEntityType(message.sourceEntityType())
                .sourceEntityId(message.sourceEntityId())
                .build();

        partyExternalRefRepository.save(externalRef);
    }

    // ==================== Validation ====================

    private void validate(PartyUpsertMessage message) {
        if (message == null) {
            throw InvalidMessageException.of("پیام نمی‌تواند خالی باشد");
        }
        if (message.messageId() == null) {
            throw InvalidMessageException.of("messageId الزامی است");
        }
        if (message.sourceService() == null || message.sourceService().isBlank()) {
            throw InvalidMessageException.of("sourceService الزامی است");
        }
        if (message.sourceEntityId() == null || message.sourceEntityId().isBlank()) {
            throw InvalidMessageException.of("sourceEntityId الزامی است");
        }
        if (message.partyType() == null) {
            throw InvalidMessageException.of("partyType الزامی است");
        }
        if (message.relationTypes() == null || message.relationTypes().isEmpty()) {
            throw InvalidMessageException.of("relationTypes الزامی است");
        }
        if (message.organizationId() == null) {
            throw InvalidMessageException.of("organizationId الزامی است");
        }
    }

    // ==================== Parsers ====================

    private IdentifierType parseIdentifierType(String identifierType) {
        try {
            return IdentifierType.valueOf(identifierType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("نوع شناسه نامعتبر است: " + identifierType);
        }
    }

    private RelationType parseRelationType(String relationType) {
        try {
            return RelationType.valueOf(relationType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("نوع رابطه نامعتبر است: " + relationType);
        }
    }
}