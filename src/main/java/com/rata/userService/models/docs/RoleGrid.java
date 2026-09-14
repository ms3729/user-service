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

import java.util.UUID;

/**
 * Document سبک فقط برای نمایش در Grid
 * اطلاعات کامل از PostgreSQL خوانده می‌شود
 */
@Document(collection = "role_grid")
@CompoundIndexes({
        // Index برای فیلتر شرکت + وضعیت + مرتب‌سازی
        @CompoundIndex(name = "idx_app", def = "{'appId': 1}"),
        // Index برای فیلتر شرکت + مرتب‌سازی نام
        @CompoundIndex(name = "idx_app_name", def = "{'appId': 1, 'name': 1}")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleGrid {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Indexed
    private long roleId;

    @Indexed
    private Integer appId;

    @Indexed
    private boolean status;

    @TextIndexed
    private String name;

    @TextIndexed
    private String code;

    @TextIndexed
    private String description;

    @Indexed
    private boolean systemRole;

    @Indexed
    private long totalCount;

}
