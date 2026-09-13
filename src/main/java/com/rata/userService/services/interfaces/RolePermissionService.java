package com.rata.userService.services.interfaces;

import com.rata.userService.models.Role;
import com.rata.userService.models.RolePermission;

import java.util.List;

public interface RolePermissionService {
    
    List<RolePermission> saveRolePermissions(Role role, List<Long> permissionIds);
    
    void deleteRolePermissions(Long roleId);
}
