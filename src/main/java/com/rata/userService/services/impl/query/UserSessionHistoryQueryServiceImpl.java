package com.rata.userService.services.impl.query;

import com.rata.userService.models.docs.UserSessionHistory;
import com.rata.userService.repositories.mongodb.UserSessionHistoryQueryRepo;
import com.rata.userService.services.interfaces.query.UserSessionHistoryQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSessionHistoryQueryServiceImpl implements UserSessionHistoryQueryService {

    private final UserSessionHistoryQueryRepo userSessionHistoryQueryRepo;
    private final HttpServletRequest request;

    @Override
    public UserSessionHistory save(String username, Date issueDate) {
        UserSessionHistory history = new UserSessionHistory();
        history.setUsername(username);
        history.setLoginDate(issueDate);
        history.setSystemInfo(request.getHeader("user-agent"));
        history.setIp(request.getHeader("X-Forwarded-For"));
        return userSessionHistoryQueryRepo.save(history);
    }

    @Override
    public List<UserSessionHistory> findAllByUsername(String username) {
        return userSessionHistoryQueryRepo.findAllByUsernameOrderByLoginDateDesc(username);
    }
}
