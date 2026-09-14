package com.rata.userService.records.newRecords;

import java.time.LocalDate;

/**
 * DTO برای نمایش در Grid
 * اطلاعات کامل از جزئیات گرفته می‌شود
 */
public record EmployeeGridResponse(
        long partyId,
        long organizationId,
        String displayName,
        String nationalCode,
        String mobile,
        String email,
        String status,
        LocalDate membershipStartDate,
        String unit
) {
}