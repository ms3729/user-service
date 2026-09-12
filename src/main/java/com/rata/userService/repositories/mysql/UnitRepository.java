package com.rata.userService.repositories.mysql;

import com.rata.userService.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Query("select u from Unit u join u.users usr where usr.user.id=:userId")
    List<Unit> findAllByUserId(long userId);

    Set<Unit> findAllByIdIn(List<Long> ides);
}
