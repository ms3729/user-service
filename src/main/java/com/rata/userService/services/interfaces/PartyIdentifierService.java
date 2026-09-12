package com.rata.userService.services.interfaces;

import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.AddIdentifierRequest;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;

public interface PartyIdentifierService {

    void save(Party party, CreateEmployeeRequest request);

    void save(Party party, AddIdentifierRequest request);

    void delete(long identifierId);
}
