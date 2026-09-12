package com.rata.userService.repositories.redis.db1;

import com.rata.userService.models.cache.zone.CityCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityCacheRepo extends CrudRepository<CityCache, Integer> {
}