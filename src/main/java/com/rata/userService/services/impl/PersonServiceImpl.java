package com.rata.userService.services.impl;

import com.rata.userService.enums.EducationLevel;
import com.rata.userService.errorHandling.BusinessException;
import com.rata.userService.models.party.Party;
import com.rata.userService.models.party.Person;
import com.rata.userService.records.newRecords.CreateEmployeeRequest;
import com.rata.userService.records.newRecords.UpdateEmployeeRequest;
import com.rata.userService.repositories.mysql.PersonRepository;
import com.rata.userService.services.interfaces.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Person save(Party party, CreateEmployeeRequest request) {
        Person person = Person.builder()
                .party(party)
                .birthDate(request.birthDate())
                .gender(request.gender())
                .educationLevel(parseEducationLevel(request.educationLevel()))
                .build();
        return personRepository.save(person);
    }

    @Override
    public void update(Party party, UpdateEmployeeRequest request) {
        Person profile = party.getPerson();
        if (request.birthDate() != null) {
            profile.setBirthDate(request.birthDate());
        }
        if (request.gender() != null) {
            profile.setGender(request.gender());
        }
        if (request.educationLevel() != null) {
            profile.setEducationLevel(parseEducationLevel(request.educationLevel()));
        }
        personRepository.save(profile);
    }

    @Override
    public void save(Party party) {
        Person profile = Person.builder()
                .party(party)
                .build();
        personRepository.save(profile);
        log.debug("Person برای partyId={} ساخته شد", party.getId());
    }

    @Override
    public void deleteByPartyId(long partyId) {
        personRepository.deleteAllByPartyId(partyId);
    }

    private EducationLevel parseEducationLevel(String educationLevel) {
        if (educationLevel == null || educationLevel.isBlank()) {
            return null;
        }
        try {
            return EducationLevel.valueOf(educationLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("سطح تحصیلات نامعتبر است: " + educationLevel);
        }
    }

    @Override
    public boolean existsById(long id) {
        return personRepository.existsById(id);
    }
}
