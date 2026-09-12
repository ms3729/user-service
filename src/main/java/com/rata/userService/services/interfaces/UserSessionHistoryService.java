package com.rata.userService.services.interfaces;

import com.rata.userService.models.docs.UserSessionHistory;

import java.util.Date;
import java.util.List;

public interface UserSessionHistoryService {

    UserSessionHistory save(String username, Date issueDate);

    List<UserSessionHistory> findAllByUsername(String username);
}
