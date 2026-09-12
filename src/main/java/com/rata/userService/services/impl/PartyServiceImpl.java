package com.rata.userService.services.impl;

import com.rata.userService.enums.PartyType;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.records.newRecords.PartyUpsertMessage;
import com.rata.userService.records.newRecords.UpdateEmployeeRequest;
import com.rata.userService.repositories.mysql.PartyRepository;
import com.rata.userService.services.interfaces.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;

    @Override
    public Party save(CreateEmployeeRequest request) {
        Party party = Party.builder()
                .partyType(PartyType.PERSON)
                .defaultLanguage(request.defaultLanguage())
                .nationalCode(request.nationalCode())
                .build();
        party = partyRepository.save(party);
        return party;
    }

    @Override
    public Party save(PartyUpsertMessage message) {
        PartyType partyType = Objects.equals(message.partyType(), PartyType.PERSON.name())
                ? PartyType.PERSON
                : PartyType.ORGANIZATION;

        Party party = Party.builder()
                .partyType(partyType)
                .defaultLanguage(message.defaultLanguage() != null
                        ? message.defaultLanguage()
                        : "fa")
                .nationalCode(message.nationalCode())
                .build();
        return partyRepository.save(party);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Party update(Party party, UpdateEmployeeRequest request) {
        if (request.defaultLanguage() != null) {
            party.setDefaultLanguage(request.defaultLanguage());
        }
        if (request.personnelCode() != null) {
            party.setPersonnelCode(request.personnelCode());
        }
        if (request.avatarUrl() != null) {
            party.setAvatarUrl(request.avatarUrl());
        }
        return partyRepository.save(party);
    }


    @Override
    public Optional<Party> findById(long id) {
        return partyRepository.findById(id);
    }

}
