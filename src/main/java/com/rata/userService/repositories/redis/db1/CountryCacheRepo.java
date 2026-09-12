package com.rata.userService.repositories.redis.db1;

import com.rata.userService.models.cache.zone.CountryCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryCacheRepo extends CrudRepository<CountryCache, Integer> {
}