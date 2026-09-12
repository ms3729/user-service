package com.rata.userService.repositories.mysql;

import com.rata.userService.enums.IdentifierType;
import com.rata.userService.models.party.PartyIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyIdentifierRepository extends JpaRepository<PartyIdentifier, Long> {

    List<PartyIdentifier> findAllByPartyId(Long partyId);

    boolean existsByPartyIdAndIdentifierTypeAndValue(
            Long partyId,
            IdentifierType identifierType,
            String value
    );

    Optional<PartyIdentifier> findByIdentifierTypeAndValue(
            IdentifierType identifierType,
            String value
    );
}
