package com.rata.userService.services.interfaces;

import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.records.newRecords.PartyUpsertMessage;
import com.rata.userService.records.newRecords.UpdateEmployeeRequest;

import java.util.Optional;

public interface PartyService {

    Party save(CreateEmployeeRequest dto);

    Party save(PartyUpsertMessage message);

    Party update(Party party, UpdateEmployeeRequest dto);

    Optional<Party> findById(long id);
}

