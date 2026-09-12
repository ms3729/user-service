package com.rata.userService.repositories.mysql;

import com.rata.userService.models.UserDenyPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDenyPermissionRepository  extends JpaRepository<UserDenyPermission, Long> {


}
