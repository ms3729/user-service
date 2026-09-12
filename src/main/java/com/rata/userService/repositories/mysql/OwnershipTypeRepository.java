package com.rata.userService.repositories.mysql;

import com.rata.userService.models.party.OwnershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnershipTypeRepository extends JpaRepository<OwnershipType, Integer> {
}
