package com.rata.userService.repositories.mysql;

import com.rata.userService.models.party.PersonTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonTranslationRepository extends JpaRepository<PersonTranslation, Long> {

    void deleteByPartyIdAndLang(long partyId, String lang);

    List<PersonTranslation> findAllByPartyId(Long partyId);

    Optional<PersonTranslation> findByPartyIdAndLang(Long partyId, String languageCode);

    boolean existsByPartyIdAndLang(Long partyId, String languageCode);

    void deleteByPartyIdAndLang(Long partyId, String languageCode);

    void deleteAllByPartyId(long partyId);
}
