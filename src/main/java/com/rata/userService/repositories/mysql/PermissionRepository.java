package com.rata.userService.repositories.mysql;

import com.rata.userService.dto.PermissionDTO;
import com.rata.userService.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository  extends JpaRepository<Permission, Long> {

    @Query(nativeQuery = true)
    Optional<PermissionDTO> findPermissionById(long id);

    @Query("select p from Permission p where p.enabled=true and p.parent is null")
    List<Permission> findAll(boolean enabled);

    @Query("SELECT DISTINCT p FROM Permission p " +
           "JOIN RolePermission rp ON rp.permission = p " +
           "JOIN UserRole ur ON ur.role = rp.role " +
           "WHERE ur.user.id = :userId")
    List<Permission> findByUserId(Long userId);
}
