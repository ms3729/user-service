package com.rata.userService.repositories.mysql;

import com.rata.userService.models.UserUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserUnitRepository extends JpaRepository<UserUnit, Long> {

    @Query("""
            SELECT uu FROM UserUnit uu
            WHERE uu.user.party.id = :partyId
              AND uu.organization.partyId = :organizationId
            """)
    Optional<UserUnit> findByPartyIdAndOrganizationId(
            @Param("partyId") Long partyId,
            @Param("organizationId") long organizationId
    );

    List<UserUnit> findAllByUserPartyId(Long partyId);

    List<UserUnit> findAllByOrganizationPartyId(long organizationId);
}
