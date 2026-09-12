package com.rata.userService.services.interfaces;

import com.rata.userService.dto.responseFiltering.PageResponse;
import com.rata.userService.models.docs.RoleGrid;
import com.rata.userService.records.CreateRoleRequest;
import com.rata.userService.records.RoleResponse;
import com.rata.userService.records.UpdateRoleRequest;
import com.rata.userService.records.newRecords.RoleSearchCriteria;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    RoleResponse createRole(CreateRoleRequest request);

    RoleResponse updateRole(Long id, UpdateRoleRequest request);

    PageResponse<RoleGrid> getRoles(RoleSearchCriteria criteria, Pageable pageable);

    RoleResponse getRole(Long id);

    void deleteRole(Long id);
}
