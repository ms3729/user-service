package com.rata.userService.repositories.redis.db1;

import com.rata.userService.models.cache.zone.StateCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateCacheRepo extends CrudRepository<StateCache, Integer> {
}