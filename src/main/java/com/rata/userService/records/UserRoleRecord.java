package com.rata.userService.records;

import java.util.List;

public record UserRoleRecord(long userId, List<Integer> rolesId) {
}
