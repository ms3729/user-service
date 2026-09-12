package com.rata.userService.repositories.mysql;

import com.rata.userService.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository  extends JpaRepository<Menu, Long> {

}
