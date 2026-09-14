package com.rata.userService.records;

import java.util.List;

public record UserProfileResponse(
        Long userId,
        String username,
        String displayName,
        String nationalCode,
        String personnelCode,
        String avatarUrl,
        List<RoleInfo> roles,
        List<PermissionInfo> permissions,
        List<IdentifierInfo> identifiers,
        List<ContactInfo> contacts
) {
    public record RoleInfo(
            Long roleId,
            String roleName,
            String roleCode,
            Long organizationId,
            String organizationName
    ) {}
    
    public record PermissionInfo(
            String appCode,
            String permissionCode,
            String permissionUrl,
            Long organizationId
    ) {}

    public record IdentifierInfo(
            Long id,
            String identifierType,
            String value,
            boolean verified
    ) {}

    public record ContactInfo(
            Long id,
            String contactType,
            String value,
            boolean isPrimary,
            boolean verified
    ) {}
}
