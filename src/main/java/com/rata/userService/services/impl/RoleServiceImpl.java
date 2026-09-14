package com.rata.userService.services.impl;

import com.rata.userService.dto.responseFiltering.PageResponse;
import com.rata.userService.errorHandling.ResourceNotFoundException;
import com.rata.userService.models.Application;
import com.rata.userService.models.Role;
import com.rata.userService.models.docs.RoleGrid;
import com.rata.userService.records.CreateRoleRequest;
import com.rata.userService.records.RoleResponse;
import com.rata.userService.records.UpdateRoleRequest;
import com.rata.userService.records.newRecords.RoleSearchCriteria;
import com.rata.userService.repositories.mongodb.RoleGridRepository;
import com.rata.userService.repositories.mysql.RoleRepository;
import com.rata.userService.services.interfaces.ApplicationService;
import com.rata.userService.services.interfaces.RolePermissionService;
import com.rata.userService.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ApplicationService applicationService;
    private final RoleGridRepository roleGridRepository;
    private final RolePermissionService rolePermissionService;

    @Override
    public RoleResponse createRole(CreateRoleRequest request) {
        Long currentOrgId = getCurrentOrganizationId();
        Long currentAppId = request.appId();
        
        Role role = new Role();
        role.setName(request.name());
        role.setDescription(request.description());
        role.setCode(request.code());
        role.setSystemRole(request.systemRole());

        if (currentAppId != null) {
            Application app = applicationService.findById(currentAppId)
                    .orElseThrow(() -> new ResourceNotFoundException("برنامه با شناسه " + currentAppId + " یافت نشد"));
            role.setApp(app);
        }

        Role savedRole = roleRepository.save(role);
        
        // Save role permissions
        if (request.permissionsId() != null && !request.permissionsId().isEmpty()) {
            rolePermissionService.saveRolePermissions(savedRole.getId(), request.permissionsId());
        }
        
        // Sync to MongoDB
        syncRoleToGrid(savedRole, 0L);
        
        return toResponse(savedRole);
    }

    @Override
    public RoleResponse updateRole(Long id, UpdateRoleRequest request) {
        Role role = getRoleById(id);
        role.setName(request.name());
        role.setDescription(request.description());
        role.setCode(request.code());
        role.setSystemRole(request.systemRole());

        if (request.appId() != null) {
            Application app = applicationService.findById(request.appId())
                    .orElseThrow(() -> new ResourceNotFoundException("برنامه با شناسه " + request.appId() + " یافت نشد"));
            role.setApp(app);
        } else {
            role.setApp(null);
        }

        Role updatedRole = roleRepository.save(role);
        
        // Update role permissions
        if (request.permissionsId() != null) {
            rolePermissionService.saveRolePermissions(updatedRole.getId(), request.permissionsId());
        }
        
        // Sync to MongoDB - user count will be updated separately
        syncRoleToGrid(updatedRole, 0L);
        
        return toResponse(updatedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleGrid> getRoles(RoleSearchCriteria criteria, Pageable pageable) {
        // Get current organization and application from security context
        Long currentOrgId = getCurrentOrganizationId();
        Long currentAppId = getCurrentApplicationId();
        
        // Override criteria with current context if not provided
        if (criteria.appId() == null && currentAppId != null) {
            criteria = new RoleSearchCriteria(currentAppId, criteria.search(), criteria.systemRole(), criteria.status());
        }
        
        Page<RoleGrid> page = roleGridRepository.searchRoles(criteria, pageable);
        return toPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRole(Long id) {
        Role role = getRoleById(id);
        return toResponse(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        
        if (role.isSystemRole()) {
            throw new RuntimeException("نقش سیستمی قابل حذف نیست");
        }
        
        // Delete role permissions first
        rolePermissionService.deleteRolePermissions(role.getId());
        
        roleRepository.delete(role);
        
        // Delete from MongoDB
        roleGridRepository.deleteByRoleId(role.getId());
    }

    private Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("نقش با شناسه " + id + " یافت نشد"));
    }

    private RoleResponse toResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCode(),
                role.isSystemRole(),
                role.isEnabled(),
                role.getApp() != null ? role.getApp().getId() : null,
                role.getApp() != null ? role.getApp().getName() : null
        );
    }

    private PageResponse<RoleGrid> toPageResponse(Page<RoleGrid> page) {
        PageResponse<RoleGrid> response = new PageResponse<>();
        response.setContent(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalSize(page.getTotalElements());
        return response;
    }
    
    private void syncRoleToGrid(Role role, long userCount) {
        RoleGrid grid = RoleGrid.builder()
                .roleId(role.getId())
                .appId(role.getApp() != null ? role.getApp().getId() : null)
                .name(role.getName())
                .code(role.getCode())
                .description(role.getDescription())
                .systemRole(role.isSystemRole())
                .status(role.isEnabled())
                .totalCount(userCount)
                .build();
        
        roleGridRepository.findByRoleId(role.getId())
                .ifPresent(existing -> grid.setId(existing.getId()));
        
        roleGridRepository.save(grid);
    }
    
    private Long getCurrentOrganizationId() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                var userDetails = (com.rata.userService.config.authentication.FlmUserDetails) authentication.getPrincipal();
                return userDetails.getOrganizationId();
            }
        } catch (Exception e) {
            // Return null if not in authenticated context
        }
        return null;
    }
    
    private Long getCurrentApplicationId() {
        // Implementation depends on how application ID is stored in security context
        // This can be extended based on your security implementation
        return null;
    }
}
