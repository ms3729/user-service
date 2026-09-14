package com.rata.userService.records;

import java.util.List;

public record UserPermissionsMenusResponse(
        List<PermissionInfo> permissions,
        List<MenuInfo> menus
) {
    public record PermissionInfo(
            String code,
            String url
    ) {}

    public record MenuInfo(
            Long id,
            String name,
            String icon,
            String url,
            String app,
            String component,
            String translationKey,
            String permission,
            List<MenuInfo> childes
    ) {}
}
