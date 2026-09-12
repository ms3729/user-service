package com.rata.userService.records.newRecords;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EmployeeEvent(
    UUID eventId,
    String eventType, // CREATED, UPDATED, DELETED, STATUS_CHANGED, etc.
    Long partyId,
    Long organizationId,
    OffsetDateTime occurredAt
) {
}