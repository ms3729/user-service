package com.rata.userService.records.newRecords;

public record RoleSearchCriteria(
        Integer appId,
        String search,
        Boolean systemRole,
        Boolean status
) {
}
