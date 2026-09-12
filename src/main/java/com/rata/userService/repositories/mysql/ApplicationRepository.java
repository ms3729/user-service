package com.rata.userService.repositories.mysql;

import com.rata.userService.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Stream<Application> findByUsersId(long userId);
}
