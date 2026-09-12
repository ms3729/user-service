package com.rata.userService.services.interfaces;

import com.rata.userService.enums.RelationType;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.AssignOrganizationRequest;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;

public interface MembershipService {

    void save(Party party, CreateEmployeeRequest request);

    void save(Party party, AssignOrganizationRequest request);

    void deleteEmployeeByPartyId(long partyId);

    void removeFromOrganization(long partyId, long organizationId);

    void isActiveEmployee(long partyId, RelationType relationType);
}
