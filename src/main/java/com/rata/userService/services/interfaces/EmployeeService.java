package com.rata.userService.services.interfaces;


import com.rata.userService.dto.responseFiltering.PageResponse;
import com.rata.userService.models.docs.EmployeeGrid;
import com.rata.userService.records.newRecords.*;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse updateEmployee(long partyId, UpdateEmployeeRequest request);

    PageResponse<EmployeeGrid> grid(EmployeeSearchCriteria filter, Pageable pageable);

    EmployeeResponse getEmployee(Long partyId);

    void deleteEmployee(long partyId);

    EmployeeResponse updateStatus(long partyId, boolean status);

    // مدیریت عضویت در شرکت‌ها
    EmployeeResponse assignToOrganization(long partyId, AssignOrganizationRequest request);

    EmployeeResponse removeFromOrganization(long partyId,long organizationId);

    // مدیریت ترجمه‌ها
    EmployeeResponse upsertTranslation(Long partyId,UpsertTranslationRequest request);

    EmployeeResponse deleteTranslation(long partyId, String languageCode);

    // مدیریت تماس‌ها
    EmployeeResponse addContact(long partyId, AddContactRequest request);

    EmployeeResponse removeContact(long partyId, long contactId);

    // مدیریت شناسه‌ها
    EmployeeResponse addIdentifier(long partyId, AddIdentifierRequest request);

    EmployeeResponse removeIdentifier(long partyId, long identifierId);
}