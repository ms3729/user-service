package com.rata.userService.repositories.mysql;

import com.rata.userService.models.party.BusinessSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessSectorRepository extends JpaRepository<BusinessSector, Integer> {
}
