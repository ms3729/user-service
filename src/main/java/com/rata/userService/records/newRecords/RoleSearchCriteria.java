package com.rata.userService.records.newRecords;

public record RoleSearchCriteria(
        Long appId,
        String search,
        Boolean systemRole,
        Boolean status
) {
}
