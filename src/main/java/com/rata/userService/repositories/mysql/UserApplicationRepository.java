package com.rata.userService.repositories.mysql;

import com.rata.userService.models.UserApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplication, Long> {

    @Query("""
            SELECT ua FROM UserApplication ua
            LEFT JOIN FETCH ua.application a
            WHERE ua.user.id = :userId
            """)
    List<UserApplication> findByUserId(@Param("userId") long userId);
}
