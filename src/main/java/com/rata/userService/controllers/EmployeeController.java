package com.rata.userService.controllers;

import com.rata.userService.records.ResponseResult;
import com.rata.userService.records.newRecords.*;
import com.rata.userService.services.interfaces.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/employees")
@Tag(name = "Employee APIs")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @Operation(summary = "get a employee acquire partyId")
    @GetMapping("/{partyId}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable long partyId) {
        EmployeeResponse response = employeeService.getEmployee(partyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "grid of employees with pagination and filter")
    @GetMapping(value = "/grid")
    public ResponseEntity<?> getEmployeeListGrid(EmployeeSearchCriteria employeeGrid, Pageable pageable) {
        return new ResponseEntity<>(new ResponseResult("", employeeService.grid(employeeGrid, pageable)), HttpStatus.OK);
    }

    @Operation(summary = "save a employee")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request
    ) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "edit a employee")
    @PutMapping(value = "/{partyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable long partyId, @Valid @RequestBody UpdateEmployeeRequest request) {
        EmployeeResponse response = employeeService.updateEmployee(partyId, request);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "delete a employee acquire partyId")
    @DeleteMapping("/{partyId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long partyId) {
        employeeService.deleteEmployee(partyId);
        return ResponseEntity.noContent().build();
    }

    // ==================== Status ====================

    @PatchMapping("/{partyId}/status")
    public ResponseEntity<EmployeeResponse> updateStatus(@PathVariable long partyId, @RequestParam boolean status) {
        EmployeeResponse response = employeeService.updateStatus(partyId, status);
        return ResponseEntity.ok(response);
    }

    // ==================== Company Membership ====================

    @PostMapping("/{partyId}/organizations")
    public ResponseEntity<EmployeeResponse> assignToCompany(@PathVariable long partyId,
                                                            @RequestParam AssignOrganizationRequest request) {
        EmployeeResponse response = employeeService.assignToOrganization(partyId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{partyId}/organizations/{organizationId}")
    public ResponseEntity<EmployeeResponse> removeFromCompany(@PathVariable long partyId, @PathVariable long organizationId) {
        EmployeeResponse response = employeeService.removeFromOrganization(partyId, organizationId);
        return ResponseEntity.ok(response);
    }

    // ==================== Translations ====================

    @PutMapping("/{partyId}/translations")
    public ResponseEntity<EmployeeResponse> upsertTranslation(@PathVariable long partyId,
                                                              @Valid @RequestBody UpsertTranslationRequest request) {
        EmployeeResponse response = employeeService.upsertTranslation(partyId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{partyId}/translations/{languageCode}")
    public ResponseEntity<EmployeeResponse> deleteTranslation(@PathVariable long partyId,
                                                              @PathVariable String languageCode) {
        EmployeeResponse response = employeeService.deleteTranslation(partyId, languageCode);
        return ResponseEntity.ok(response);
    }

    // ==================== Contacts ====================

    @PostMapping("/{partyId}/contacts")
    public ResponseEntity<EmployeeResponse> addContact(@PathVariable long partyId,
                                                       @Valid @RequestBody AddContactRequest request) {
        EmployeeResponse response = employeeService.addContact(partyId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{partyId}/contacts/{contactId}")
    public ResponseEntity<EmployeeResponse> removeContact(@PathVariable long partyId, @PathVariable long contactId) {
        EmployeeResponse response = employeeService.removeContact(partyId, contactId);
        return ResponseEntity.ok(response);
    }

    // ==================== Identifiers ====================

    @PostMapping("/{partyId}/identifiers")
    public ResponseEntity<EmployeeResponse> addIdentifier(@PathVariable long partyId,
                                                          @Valid @RequestBody AddIdentifierRequest request) {
        EmployeeResponse response = employeeService.addIdentifier(partyId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{partyId}/identifiers/{identifierId}")
    public ResponseEntity<EmployeeResponse> removeIdentifier(@PathVariable long partyId,
                                                             @PathVariable long identifierId) {
        EmployeeResponse response = employeeService.removeIdentifier(partyId, identifierId);
        return ResponseEntity.ok(response);
    }
}
