package com.rata.userService.repositories.mongodb.employee;

import com.rata.userService.models.docs.EmployeeGrid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeGridRepository extends MongoRepository<EmployeeGrid, String>, EmployeeGridCustomRepository {

    Optional<EmployeeGrid> findByPartyIdAndOrganizationId(long partyId, long organizationId);

    List<EmployeeGrid> findAllByOrganizationId(long organizationId);

    List<EmployeeGrid> findAllByPartyId(long partyId);

    List<EmployeeGrid> findAllByPartyIdIn(List<Long> partiesId);

    void deleteByPartyIdAndOrganizationId(long partyId, long organizationId);

    void deleteAllByPartyId(long partyId);

    boolean existsByPartyIdAndOrganizationId(long partyId, long organizationId);

    @Query(value = "{ 'organizationId': ?0, 'status': ?1 }")
    List<EmployeeGrid> findByOrganizationIdAndStatus(long organizationId, String status);
}