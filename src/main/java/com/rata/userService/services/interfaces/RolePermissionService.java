package com.rata.userService.services.interfaces;

import com.rata.userService.models.RolePermission;

import java.util.List;

public interface RolePermissionService {
    
    List<RolePermission> saveRolePermissions(Long roleId, List<Long> permissionIds);
    
    void deleteRolePermissions(Long roleId);
}
