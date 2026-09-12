package com.rata.userService.repositories.mysql;

import com.rata.userService.models.party.OrganizationTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationTranslationRepository extends JpaRepository<OrganizationTranslation, Long> {

    Optional<OrganizationTranslation> findByPartyIdAndLang(long id, String languageCode);
}
