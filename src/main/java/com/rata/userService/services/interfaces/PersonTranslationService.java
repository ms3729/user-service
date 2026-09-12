package com.rata.userService.services.interfaces;


import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.records.newRecords.PartyUpsertMessage;
import com.rata.userService.records.newRecords.UpdateEmployeeRequest;
import com.rata.userService.records.newRecords.UpsertTranslationRequest;

public interface PersonTranslationService {

    void save(Party party, CreateEmployeeRequest userDTO);

    void save(Party party, UpdateEmployeeRequest request);

    void save(Party party, UpsertTranslationRequest request);

    void save(Party party, PartyUpsertMessage message);

    void deleteByPartyIdAndLang(long partyId, String languageCode);

    void deleteByPartyId(long partyId);
}
