package com.rata.userService.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoleRequest(
        @NotBlank(message = "نام نقش الزامی است")
        String name,

        String description,

        @NotBlank(message = "کد نقش الزامی است")
        String code,

        boolean systemRole,

        Long appId
) {
}
