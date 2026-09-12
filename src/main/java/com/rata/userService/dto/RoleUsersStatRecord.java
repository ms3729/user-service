package com.rata.userService.dto;


import java.util.List;

public record RoleUsersStatRecord(long usersCount, List<String> users) {
}
