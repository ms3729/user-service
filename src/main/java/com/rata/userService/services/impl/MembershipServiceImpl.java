package com.rata.userService.services.impl;

import com.rata.userService.enums.RelationType;
import com.rata.userService.errorHandling.BusinessException;
import com.rata.userService.errorHandling.ResourceNotFoundException;
import com.rata.userService.models.party.Membership;
import com.rata.userService.models.party.Organization;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.AssignOrganizationRequest;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.repositories.mysql.MembershipRepository;
import com.rata.userService.rules.MembershipRulesService;
import com.rata.userService.services.interfaces.MembershipService;
import com.rata.userService.services.interfaces.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final OrganizationService organizationService;
    private final MembershipRulesService membershipRulesService;

    @Override
    public void save(Party party, CreateEmployeeRequest request) {
        for (AssignOrganizationRequest company : request.organizations()) {
            saveMembership(party, company);
        }
    }

    @Override
    public void save(Party party, AssignOrganizationRequest request) {
        saveMembership(party, request);
    }

    @Override
    public void deleteEmployeeByPartyId(long partyId) {
        membershipRepository.deleteAll(membershipRepository.findAllByPartyIdAndRelationType(
                partyId, RelationType.EMPLOYEE));
    }

    @Override
    public void removeFromOrganization(long partyId, long organizationId) {
        Membership membership = membershipRepository
                .findByPartyIdAndOrganizationPartyIdAndRelationType(
                        partyId, organizationId, RelationType.EMPLOYEE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "کارمند به این شرکت متصل نیست"));

        membershipRepository.delete(membership);
    }

    @Override
    public void isActiveEmployee(long partyId, RelationType relationType) {
        // بررسی اینکه این Party واقعاً یک کارمند است
        if (!membershipRepository.hasActiveMembership(partyId, RelationType.EMPLOYEE)) {
            throw new BusinessException("این Party یک کارمند نیست");
        }
    }

    private void saveMembership(Party party, AssignOrganizationRequest request) {
        membershipRulesService.checkOrganizationMembership(party.getId(), request.organizationId());
        Optional<Organization> organization = organizationService.findById(request.organizationId());
        if (organization.isPresent()) {
            Membership membership = Membership.builder()
                    .party(party)
                    .organization(organization.get())
                    .relationType(RelationType.EMPLOYEE)
                    .startDate(request.startDate())
                    .endDate(request.endDate())
                    .build();
            membershipRepository.save(membership);
        }
    }
}
