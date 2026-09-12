package com.rata.userService.services.interfaces;

import com.rata.userService.models.Application;
import com.rata.userService.records.ApplicationRecord;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {

    List<ApplicationRecord> findByUserId(long userId);
    
    Optional<Application> findById(Long id);
}
