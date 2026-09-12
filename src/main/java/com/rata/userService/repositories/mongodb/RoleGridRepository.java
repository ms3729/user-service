package com.rata.userService.repositories.mongodb;

import com.rata.userService.models.docs.RoleGrid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleGridRepository extends MongoRepository<RoleGrid, String>, RoleGridCustomRepository {

    Optional<RoleGrid> findByRoleId(long roleId);

    List<RoleGrid> findAllByAppId(Long appId);

    void deleteByRoleId(long roleId);

    boolean existsByRoleId(long roleId);
}
