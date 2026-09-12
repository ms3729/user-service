package com.rata.userService.services.impl;

import com.rata.userService.enums.ContactType;
import com.rata.userService.errorHandling.BusinessException;
import com.rata.userService.errorHandling.DuplicateResourceException;
import com.rata.userService.models.party.Party;
import com.rata.userService.models.party.PartyContact;
import com.rata.userService.records.newRecords.AddContactRequest;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.repositories.mysql.PartyContactRepository;
import com.rata.userService.services.interfaces.PartyContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PartyContactServiceImpl implements PartyContactService {

    private final PartyContactRepository partyContactRepository;

    @Override
    public void save(Party party, CreateEmployeeRequest request) {
        for (AddContactRequest contactRecord : request.contacts()) {
            save(party, contactRecord);
        }
    }

    @Override
    public void save(Party party, AddContactRequest request) {
        ContactType contactType = parseContactType(request.contactType());
        boolean exists = partyContactRepository
                .existsByPartyIdAndContactTypeAndValue(
                        party.getId(), contactType, request.value());
        if (exists) {
            throw new DuplicateResourceException(
                    "راه تماس تکراری است: " + request.value());
        }

        PartyContact contact = PartyContact.builder()
                .party(party)
                .contactType(contactType)
                .value(request.value())
                .isPrimary(request.isPrimary())
                .verified(request.verified())
                .build();

        partyContactRepository.save(contact);
    }

    private ContactType parseContactType(String contactType) {
        try {
            return ContactType.valueOf(contactType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("نوع تماس نامعتبر است: " + contactType);
        }
    }

    @Override
    public void deleteByPartyId(long partyId) {
        partyContactRepository.deleteAllByPartyId(partyId);
    }

    @Override
    public void delete(long contactId) {
        partyContactRepository.deleteById(contactId);
    }
}
