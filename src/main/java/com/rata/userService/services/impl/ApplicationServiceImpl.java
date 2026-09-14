package com.rata.userService.services.impl;

import com.rata.userService.models.Application;
import com.rata.userService.models.UserApplication;
import com.rata.userService.records.ApplicationRecord;
import com.rata.userService.repositories.mysql.ApplicationRepository;
import com.rata.userService.repositories.mysql.UserApplicationRepository;
import com.rata.userService.services.interfaces.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserApplicationRepository userApplicationRepository;

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

    @Override
    @Transactional
    public List<ApplicationRecord> findApplicationsByUserId(long userId) {
        List<UserApplication> userApplications = userApplicationRepository.findByUserId(userId);
        return userApplications.stream()
                .map(ua -> {
                    Application app = ua.getApplication();
                    return new ApplicationRecord(
                            app.getId(),
                            app.getName(),
                            app.getCode(),
                            app.getIcon(),
                            app.getUrl(),
                            app.getDescription(),
                            app.getGradient()
                    );
                })
                .collect(Collectors.toList());
    }
}
