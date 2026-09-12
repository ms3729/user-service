package com.rata.userService.repositories.mysql;

import com.rata.userService.enums.PartyType;
import com.rata.userService.models.party.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    @Query("""
            SELECT p FROM Party p
            LEFT JOIN FETCH p.person pp
            LEFT JOIN FETCH p.personTranslations
            LEFT JOIN FETCH p.organizationTranslations
            LEFT JOIN FETCH p.identifiers
            LEFT JOIN FETCH p.contacts
            LEFT JOIN FETCH p.memberships
            WHERE p.id = :partyId
            """)
    Optional<Party> findByIdWithDetails(@Param("partyId") long partyId);

    boolean existsByPartyTypeAndEnabled(PartyType partyType, boolean enabled);
}
