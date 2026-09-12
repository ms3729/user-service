package com.rata.userService.services.interfaces;

import com.rata.userService.dto.PermissionDTO;
import com.rata.userService.models.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService  {

    Optional<PermissionDTO> findById(long id);

    List<PermissionDTO> findAllByEnabledStatus(boolean enabled);
}
