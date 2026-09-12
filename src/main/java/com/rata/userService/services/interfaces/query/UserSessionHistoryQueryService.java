package com.rata.userService.services.interfaces.query;

import com.rata.userService.models.docs.UserSessionHistory;

import java.util.Date;
import java.util.List;

public interface UserSessionHistoryQueryService {

    UserSessionHistory save(String username, Date issueDate);

    List<UserSessionHistory> findAllByUsername(String username);
}
