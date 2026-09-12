package com.rata.userService.repositories.mysql;

import com.rata.userService.models.party.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    void deleteAllByPartyId(long partyId);
}
