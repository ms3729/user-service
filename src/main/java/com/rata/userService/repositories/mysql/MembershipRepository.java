package com.rata.userService.repositories.mysql;

import com.rata.userService.enums.RelationType;
import com.rata.userService.models.party.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findAllByPartyIdAndRelationType(Long partyId, RelationType relationType);

    Optional<Membership> findByPartyIdAndOrganizationPartyIdAndRelationType(
            Long partyId,
            long organizationId,
            RelationType relationType
    );

    // متد جدید: همه membership های یک Party در یک شرکت
    List<Membership> findAllByPartyIdAndOrganizationPartyId(Long partyId, long organizationId);

    boolean existsByPartyIdAndRelationType(Long partyId, RelationType relationType);

    boolean existsByPartyIdAndOrganizationPartyIdAndRelationType(
            Long partyId,
            long organizationId,
            RelationType relationType
    );

    @Query("""
            SELECT COUNT(m) > 0 FROM Membership m
            WHERE m.party.id = :partyId
              AND m.relationType = :relationType
            """)
    boolean hasActiveMembership(
            @Param("partyId") Long partyId,
            @Param("relationType") RelationType relationType
    );

    @Query("""
            SELECT m FROM Membership m
            WHERE m.party.id = :partyId
              AND m.organization.partyId = :organizationId
            """)
    List<Membership> findActiveByPartyIdAndOrganizationId(
            @Param("partyId") Long partyId,
            @Param("organizationId") long organizationId
    );

    long countByPartyIdAndRelationType(Long partyId, RelationType relationType);

}