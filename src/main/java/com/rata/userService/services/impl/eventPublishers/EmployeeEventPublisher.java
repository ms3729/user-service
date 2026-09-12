package com.rata.userService.services.impl.eventPublishers;

import com.rata.userService.records.newRecords.EmployeeEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class EmployeeEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public EmployeeEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEmployeeCreated(Long partyId, long organizationId) {
        publishEvent("CREATED", partyId, organizationId);
    }

    public void publishEmployeeUpdated(Long partyId, long organizationId) {
        publishEvent("UPDATED", partyId, organizationId);
    }

    public void publishEmployeeDeleted(Long partyId, long organizationId) {
        publishEvent("DELETED", partyId, organizationId);
    }

    public void publishEmployeeStatusChanged(Long partyId, long organizationId) {
        publishEvent("STATUS_CHANGED", partyId, organizationId);
    }

    public void publishEmployeeOrganizationAssigned(Long partyId, long organizationId) {
        publishEvent("ORGANIZATION_ASSIGNED", partyId, organizationId);
    }

    public void publishEmployeeOrganizationRemoved(Long partyId, long organizationId) {
        publishEvent("ORGANIZATION_REMOVED", partyId, organizationId);
    }

    private void publishEvent(String eventType, Long partyId, long organizationId) {
        EmployeeEvent event = new EmployeeEvent(
                UUID.randomUUID(),
                eventType,
                partyId,
                organizationId,
                OffsetDateTime.now()
        );

        eventPublisher.publishEvent(event);
    }
}