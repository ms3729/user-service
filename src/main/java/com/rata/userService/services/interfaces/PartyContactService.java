package com.rata.userService.services.interfaces;

import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.AddContactRequest;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;

public interface PartyContactService {

    void save(Party party, CreateEmployeeRequest request);

    void save(Party party, AddContactRequest request);

    void deleteByPartyId(long partyId);

    void delete(long contactId);
}
