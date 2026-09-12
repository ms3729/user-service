package com.rata.userService.repositories.mysql;

import com.rata.userService.enums.ContactType;
import com.rata.userService.models.party.PartyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyContactRepository extends JpaRepository<PartyContact, Long> {

    List<PartyContact> findAllByPartyId(Long partyId);

    boolean existsByPartyIdAndContactTypeAndValue(
            Long partyId,
            ContactType contactType,
            String value
    );

    void deleteAllByPartyId(long partyId);
}
