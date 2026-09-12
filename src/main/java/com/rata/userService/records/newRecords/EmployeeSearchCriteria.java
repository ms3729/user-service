package com.rata.userService.records.newRecords;

public record EmployeeSearchCriteria(
        Long organizationId,
        String search,
        String gender
) {
}