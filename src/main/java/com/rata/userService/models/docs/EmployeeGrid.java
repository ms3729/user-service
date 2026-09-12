package com.rata.userService.models.docs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/**
 * Document سبک فقط برای نمایش در Grid
 * اطلاعات کامل از PostgreSQL خوانده می‌شود
 */
@Document(collection = "employee_grid")
@CompoundIndexes({
        // Index برای فیلتر شرکت + وضعیت + مرتب‌سازی
        @CompoundIndex(name = "idx_organization", def = "{'organizationId': 1}"),
        // Index برای فیلتر شرکت + مرتب‌سازی نام
        @CompoundIndex(name = "idx_organization_displayName", def = "{'organizationId': 1, 'displayName': 1}"),
        // Index برای فیلتر شرکت + مرتب‌سازی تاریخ عضویت
        @CompoundIndex(name = "idx_organization_membershipStart", def = "{'organizationId': 1, 'membershipStartDate': -1}")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeGrid {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Indexed
    private long partyId;

    @Indexed
    private long organizationId;

    @Indexed
    private boolean status;

    @TextIndexed
    private String displayName;

    // نام‌ها به زبان‌های مختلف برای جستجوی چندزبانه
    private Map<String, String> displayNames;

    // فیلدهای denormalized برای نمایش سریع
    private String nationalCode;
    private String mobile;
    private String email;

    // تاریخ عضویت در شرکت
    private LocalDate membershipStartDate;

}