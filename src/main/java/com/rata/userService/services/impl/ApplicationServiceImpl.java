package com.rata.userService.services.impl;

import com.rata.userService.models.Application;
import com.rata.userService.records.ApplicationRecord;
import com.rata.userService.repositories.mysql.ApplicationRepository;
import com.rata.userService.services.interfaces.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public List<ApplicationRecord> findByUserId(long userId) {
        return applicationRepository.findByUsersId(userId).map(a ->
                        new ApplicationRecord(a.getId(), a.getName(), a.getCode(), a.getIcon(), a.getUrl(), a.getDescription(), a.getGradient()))
                .toList();
    }

    @Override
    @Transactional
    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }
}
