package com.rata.userService.repositories.mysql;

import com.rata.userService.models.party.PartyExternalRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyExternalRefRepository extends JpaRepository<PartyExternalRef, Long> {

    Optional<PartyExternalRef> findBySourceServiceAndSourceEntityId(
            String sourceService,
            String sourceEntityId
    );

    boolean existsBySourceServiceAndSourceEntityId(
            String sourceService,
            String sourceEntityId
    );
}