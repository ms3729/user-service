package com.rata.userService.services.interfaces;

import com.rata.userService.records.ApplicationRecord;

import java.util.List;

public interface ApplicationService {

    List<ApplicationRecord> findByUserId(long userId);
}
