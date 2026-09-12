package com.rata.userService.repositories.redis.db0;

import com.rata.userService.models.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenCommandRepo extends CrudRepository<Token, String> {
}
