package com.rata.userService.services.impl;

import com.rata.userService.errorHandling.ResourceNotFoundException;
import com.rata.userService.models.Permission;
import com.rata.userService.models.Role;
import com.rata.userService.models.RolePermission;
import com.rata.userService.repositories.mysql.PermissionRepository;
import com.rata.userService.repositories.mysql.RolePermissionRepository;
import com.rata.userService.services.interfaces.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<RolePermission> saveRolePermissions(Role role, List<Long> permissionIds) {
        // Delete existing permissions for this role
        deleteRolePermissions(role.getId());

        if (permissionIds == null || permissionIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<RolePermission> savedPermissions = new ArrayList<>();

        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new ResourceNotFoundException("دسترسی با شناسه " + permissionId + " یافت نشد"));

            RolePermission rolePermission = new RolePermission(role, permission);
            savedPermissions.add(rolePermissionRepository.save(rolePermission));
        }

        return savedPermissions;
    }

    @Override
    @Transactional
    public void deleteRolePermissions(Long roleId) {
        rolePermissionRepository.deleteByRoleId(roleId);
    }
}
