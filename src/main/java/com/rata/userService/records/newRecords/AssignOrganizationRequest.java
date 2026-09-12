package com.rata.userService.records.newRecords;

import java.time.LocalDate;

public record AssignOrganizationRequest(
        long organizationId,
        LocalDate startDate,
        LocalDate endDate
) {
}