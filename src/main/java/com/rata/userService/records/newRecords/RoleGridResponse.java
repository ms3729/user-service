package com.rata.userService.records.newRecords;

/**
 * DTO برای نمایش در Grid
 * اطلاعات کامل از جزئیات گرفته می‌شود
 */
public record RoleGridResponse(
        long roleId,
        String name,
        String code,
        String description,
        boolean systemRole,
        boolean status,
        Long appId,
        String appName
) {
}
