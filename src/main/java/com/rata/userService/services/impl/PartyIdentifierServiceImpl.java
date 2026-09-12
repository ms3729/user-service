package com.rata.userService.services.impl;

import com.rata.userService.enums.IdentifierType;
import com.rata.userService.errorHandling.BusinessException;
import com.rata.userService.errorHandling.DuplicateResourceException;
import com.rata.userService.models.party.Party;
import com.rata.userService.models.party.PartyIdentifier;
import com.rata.userService.records.newRecords.AddIdentifierRequest;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.repositories.mysql.PartyIdentifierRepository;
import com.rata.userService.services.interfaces.PartyIdentifierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PartyIdentifierServiceImpl implements PartyIdentifierService {

    private final PartyIdentifierRepository partyIdentifierRepository;

    @Override
    public void save(Party party, CreateEmployeeRequest request) {
        for (AddIdentifierRequest ident : request.identifiers()) {
            save(party, ident);
        }
    }

    @Override
    public void save(Party party, AddIdentifierRequest ident) {
        IdentifierType identifierType = parseIdentifierType(ident.identifierType());
        // بررسی تکراری نبودن
        boolean exists = partyIdentifierRepository
                .existsByPartyIdAndIdentifierTypeAndValue(
                        party.getId(), identifierType, ident.value());
        if (exists) {
            throw new DuplicateResourceException("شناسه تکراری است: " + ident.value());
        }
        PartyIdentifier identifier = PartyIdentifier.builder()
                .party(party)
                .identifierType(identifierType)
                .value(ident.value())
                .verified(ident.verified())
                .build();

        partyIdentifierRepository.save(identifier);
    }

    @Override
    public void delete(long identifierId) {
        partyIdentifierRepository.deleteById(identifierId);
    }

    private IdentifierType parseIdentifierType(String identifierType) {
        try {
            return IdentifierType.valueOf(identifierType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("نوع شناسه نامعتبر است: " + identifierType);
        }
    }
}
