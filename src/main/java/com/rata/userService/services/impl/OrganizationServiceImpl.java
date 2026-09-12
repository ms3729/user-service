package com.rata.userService.services.impl;

import com.rata.userService.models.party.Organization;
import com.rata.userService.models.party.Party;
import com.rata.userService.repositories.mysql.OrganizationRepository;
import com.rata.userService.services.interfaces.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public void save(Party party) {
        Organization profile = Organization.builder()
                .party(party)
                .build();
        organizationRepository.save(profile);
        log.debug("Organization برای partyId={} ساخته شد", party.getId());
    }

    @Override
    public Optional<Organization> findById(long orgId) {
        return organizationRepository.findById(orgId);
    }

    @Override
    public boolean existsById(long id) {
        return organizationRepository.existsById(id);
    }
}
