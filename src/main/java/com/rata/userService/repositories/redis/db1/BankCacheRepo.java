package com.rata.userService.repositories.redis.db1;

import com.rata.userService.models.cache.BankCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankCacheRepo extends CrudRepository<BankCache, Integer> {

    Optional<BankCache> findByCode(String code);
}