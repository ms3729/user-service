package com.rata.userService.services.interfaces;

import com.rata.userService.models.party.Organization;
import com.rata.userService.models.party.Party;

import java.util.Optional;

public interface OrganizationService {

    void save(Party party);

    Optional<Organization> findById(long orgId);

    boolean existsById(long id);
}
