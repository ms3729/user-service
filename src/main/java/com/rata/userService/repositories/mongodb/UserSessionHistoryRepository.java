package com.rata.userService.repositories.mongodb;

import com.rata.userService.models.docs.UserSessionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionHistoryRepository extends MongoRepository<UserSessionHistory, Long> {

    List<UserSessionHistory> findAllByUsernameOrderByLoginDateDesc(String username);
}
