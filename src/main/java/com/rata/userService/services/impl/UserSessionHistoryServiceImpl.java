package com.rata.userService.services.impl;

import com.rata.userService.models.docs.UserSessionHistory;
import com.rata.userService.repositories.mongodb.UserSessionHistoryRepository;
import com.rata.userService.services.interfaces.UserSessionHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSessionHistoryServiceImpl implements UserSessionHistoryService {

    private final UserSessionHistoryRepository userSessionHistoryRepository;
    private final HttpServletRequest request;

    @Override
    public UserSessionHistory save(String username, Date issueDate) {
        UserSessionHistory history = new UserSessionHistory();
        history.setUsername(username);
        history.setLoginDate(issueDate);
        history.setSystemInfo(request.getHeader("user-agent"));
        history.setIp(request.getHeader("X-Forwarded-For"));
        return userSessionHistoryRepository.save(history);
    }

    @Override
    public List<UserSessionHistory> findAllByUsername(String username) {
        return userSessionHistoryRepository.findAllByUsernameOrderByLoginDateDesc(username);
    }
}
