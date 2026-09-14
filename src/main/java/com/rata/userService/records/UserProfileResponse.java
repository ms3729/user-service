package com.rata.userService.records;

import java.util.List;

public record UserProfileResponse(
        Long userId,
        String username,
        String displayName,
        String nationalCode,
        String personnelCode,
        String avatarUrl,
        List<RoleInfo> roles
) {
    public record RoleInfo(
            Long roleId,
            String roleName,
            String roleCode,
            Long organizationId,
            String organizationName
    ) {}
}
