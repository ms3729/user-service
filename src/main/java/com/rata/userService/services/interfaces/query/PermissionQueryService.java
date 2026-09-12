package com.rata.userService.services.interfaces.query;

import com.rata.userService.dto.PermissionDTO;
import com.rata.userService.models.Permission;
import com.rata.userService.services.interfaces.BasicQueryService;

import java.util.List;
import java.util.Optional;

public interface PermissionQueryService  extends BasicQueryService<Permission, Long> {

    Optional<PermissionDTO> findById(long id);

    List<PermissionDTO> findAllByEnabledStatus(boolean enabled);
}
