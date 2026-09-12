package com.rata.userService.rules;

import com.rata.userService.enums.RelationType;
import com.rata.userService.errorHandling.DuplicateResourceException;
import com.rata.userService.repositories.mysql.MembershipRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MembershipRulesService {

    private final MembershipRepository membershipRepository;

    public void checkOrganizationMembership(long partyId, long organizationId) {
        boolean exists = membershipRepository
                .findByPartyIdAndOrganizationPartyIdAndRelationType(
                        partyId, organizationId, RelationType.EMPLOYEE)
                .isPresent();
        if (exists) {
            throw new DuplicateResourceException("کارمند قبلاً به این شرکت متصل شده است");
        }
    }
}
