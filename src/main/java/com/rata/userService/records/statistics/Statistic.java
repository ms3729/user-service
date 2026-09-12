package com.rata.userService.records.statistics;

public record Statistic(long totalUser, long activeUsers, long inactiveUsers,
                        long drivers, long customers, long owner) {
}
