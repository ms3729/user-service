package com.rata.userService.services.impl.listeners;

import com.rata.userService.enums.ContactType;
import com.rata.userService.enums.PartyType;
import com.rata.userService.enums.RelationType;
import com.rata.userService.models.docs.EmployeeGrid;
import com.rata.userService.models.party.Membership;
import com.rata.userService.models.party.Party;
import com.rata.userService.models.party.PartyContact;
import com.rata.userService.models.party.PersonTranslation;
import com.rata.userService.records.newRecords.EmployeeEvent;
import com.rata.userService.repositories.mongodb.EmployeeGridRepository;
import com.rata.userService.repositories.mysql.MembershipRepository;
import com.rata.userService.repositories.mysql.PartyContactRepository;
import com.rata.userService.repositories.mysql.PartyRepository;
import com.rata.userService.repositories.mysql.PersonTranslationRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class EmployeeGridEventListener {

    private final PartyRepository partyRepository;
    private final PersonTranslationRepository personTranslationRepository;
    private final PartyContactRepository partyContactRepository;
    private final MembershipRepository membershipRepository;
    private final EmployeeGridRepository employeeGridMongoRepository;


    @Async
    @TransactionalEventListener
    public void handleEmployeeEvent(EmployeeEvent event) {
        switch (event.eventType()) {
            case "CREATED", "UPDATED", "STATUS_CHANGED", "ORGANIZATION_ASSIGNED" ->
                    syncToMongoDB(event.partyId(), event.organizationId());

            case "DELETED" -> employeeGridMongoRepository.deleteAllByPartyId(event.partyId());

            case "ORGANIZATION_REMOVED" -> employeeGridMongoRepository.deleteByPartyIdAndOrganizationId(
                    event.partyId(), event.organizationId());
        }
    }

    private void syncToMongoDB(Long partyId, Long organizationId) {
        // بارگذاری Party از PostgreSQL
        Party party = partyRepository.findByIdWithDetails(partyId).orElse(null);

        if (party == null || party.getPartyType() != PartyType.PERSON) {
            return;
        }

        // بررسی اینکه کارمند است
        boolean isEmployee = membershipRepository.hasActiveMembership(
                partyId, RelationType.EMPLOYEE);

        if (!isEmployee) {
            return;
        }

        // پیدا کردن همه شرکت‌هایی که این کارمند به آن‌ها متصل است
        List<Long> organizations = organizationId != null
                ? List.of(organizationId)
                : membershipRepository.findAllByPartyIdAndRelationType(
                        partyId, RelationType.EMPLOYEE)
                .stream()
                .map(m -> m.getOrganization().getPartyId())
                .toList();

        // ساخت EmployeeGrid برای هر شرکت
        for (Long orgId : organizations) {
            EmployeeGrid grid = buildGrid(party, orgId);
            employeeGridMongoRepository.save(grid);
        }
    }

    private EmployeeGrid buildGrid(Party party, long organizationId) {
        // استخراج نام‌های چندزبانه
        Map<String, String> displayNames = new HashMap<>();
        List<PersonTranslation> translations = personTranslationRepository
                .findAllByPartyId(party.getId());

        for (PersonTranslation translation : translations) {
            displayNames.put(translation.getLang(), translation.getFirstname().trim() + translation.getLastname().trim());
        }

        // استخراج فیلدهای denormalized

        List<PartyContact> contacts = partyContactRepository
                .findAllByPartyId(party.getId());

        String mobile = contacts.stream()
                .filter(c -> c.getContactType() == ContactType.MOBILE)
                .map(PartyContact::getValue)
                .findFirst()
                .orElse(null);

        String email = contacts.stream()
                .filter(c -> c.getContactType() == ContactType.EMAIL)
                .map(PartyContact::getValue)
                .findFirst()
                .orElse(null);

        // پیدا کردن Membership برای این شرکت
        Membership membership = membershipRepository
                .findByPartyIdAndOrganizationPartyIdAndRelationType(
                        party.getId(), organizationId, RelationType.EMPLOYEE)
                .orElse(null);

        return EmployeeGrid.builder()
                .id(party.getId() + ":" + organizationId)
                .partyId(party.getId())
                .organizationId(organizationId)
                .status(party.isEnabled())
                .displayName(party.getDisplayName())
                .displayNames(displayNames)
                .nationalCode(party.getNationalCode())
                .mobile(mobile)
                .email(email)
                .membershipStartDate(membership != null && membership.getStartDate() != null
                        ? membership.getStartDate()
                        : null)
                .build();
    }

}