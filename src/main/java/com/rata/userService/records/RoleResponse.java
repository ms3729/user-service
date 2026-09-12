package com.rata.userService.records;

public record RoleResponse(
        Long id,
        String name,
        String description,
        String code,
        boolean systemRole,
        boolean enabled,
        Long appId,
        String appName
) {
}
