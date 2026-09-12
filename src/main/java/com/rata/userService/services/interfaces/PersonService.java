package com.rata.userService.services.interfaces;

import com.rata.userService.models.party.Party;
import com.rata.userService.models.party.Person;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.records.newRecords.UpdateEmployeeRequest;

public interface PersonService {

    Person save(Party party, CreateEmployeeRequest request);

    void update(Party party, UpdateEmployeeRequest request);

    void save(Party party);

    void deleteByPartyId(long partyId);

    boolean existsById(long id);
}
