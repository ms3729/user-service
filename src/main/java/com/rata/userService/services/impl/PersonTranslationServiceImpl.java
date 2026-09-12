package com.rata.userService.services.impl;

import com.rata.userService.models.party.Party;
import com.rata.userService.models.party.PersonTranslation;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.records.newRecords.PartyUpsertMessage;
import com.rata.userService.records.newRecords.UpdateEmployeeRequest;
import com.rata.userService.records.newRecords.UpsertTranslationRequest;
import com.rata.userService.repositories.mysql.PersonTranslationRepository;
import com.rata.userService.services.interfaces.PersonTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PersonTranslationServiceImpl implements PersonTranslationService {

    private final PersonTranslationRepository ptRepository;

    @Override
    public void save(Party party, CreateEmployeeRequest request) {
        for (UpsertTranslationRequest translation : request.translations()) {
            saveTranslation(party, translation);
        }
    }

    @Override
    public void save(Party party, UpdateEmployeeRequest request) {
        for (UpsertTranslationRequest translation : request.translations()) {
            saveTranslation(party, translation);
        }
    }

    @Override
    public void save(Party party, UpsertTranslationRequest request) {
        saveTranslation(party, request);
    }

    @Override
    public void save(Party party, PartyUpsertMessage message) {
        for (Map.Entry<String, PartyUpsertMessage.PersonNameDto> entry :
                message.personTranslations().entrySet()) {
            String languageCode = entry.getKey();
            PartyUpsertMessage.PersonNameDto dto = entry.getValue();
            PersonTranslation translation = ptRepository
                    .findByPartyIdAndLang(party.getId(), languageCode)
                    .orElseGet(() -> {
                        PersonTranslation newTranslation = new PersonTranslation();
                        newTranslation.setParty(party);
                        newTranslation.setLang(languageCode);
                        return newTranslation;
                    });

            translation.setFirstname(dto.firstName());
            translation.setLastname(dto.lastName());
            ptRepository.save(translation);
        }
    }

    @Override
    public void deleteByPartyIdAndLang(long partyId, String languageCode) {
        ptRepository.deleteByPartyIdAndLang(partyId, languageCode);
    }

    @Override
    public void deleteByPartyId(long partyId) {
        ptRepository.deleteAllByPartyId(partyId);
    }


    private void saveTranslation(Party party, UpsertTranslationRequest request) {
        PersonTranslation translation = ptRepository.findByPartyIdAndLang(party.getId(), request.lang())
                .orElseGet(() -> {
                    PersonTranslation newTranslation = new PersonTranslation();
                    newTranslation.setParty(party);
                    newTranslation.setLang(request.lang());
                    return newTranslation;
                });

        translation.setFirstname(request.firstName());
        translation.setLastname(request.lastName());
        translation.setFatherName(request.fatherName());
        ptRepository.save(translation);
    }
}
