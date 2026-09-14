package com.rata.userService.repositories.mysql;

import com.rata.userService.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository  extends JpaRepository<Menu, Long> {

    @Query("SELECT DISTINCT m FROM Menu m " +
           "JOIN m.permission p " +
           "JOIN RolePermission rp ON rp.permission = p " +
           "JOIN UserRole ur ON ur.role = rp.role " +
           "WHERE ur.user.id = :userId " +
           "ORDER BY m.id")
    List<Menu> findByUserId(Long userId);
}
