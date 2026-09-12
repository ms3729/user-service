package com.rata.userService.controllers;

import com.rata.userService.records.ResponseResult;
import com.rata.userService.records.newRecords.RoleSearchCriteria;
import com.rata.userService.services.interfaces.RoleService;
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
@RequestMapping("/roles")
@Tag(name = "Role APIs")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "create a new role")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRole(
            @Valid @RequestBody CreateRoleRequest request
    ) {
        return new ResponseEntity<>(new ResponseResult("", roleService.createRole(request)), HttpStatus.CREATED);
    }

    @Operation(summary = "update an existing role")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
        return new ResponseEntity<>(new ResponseResult("", roleService.updateRole(id, request)), HttpStatus.OK);
    }

    @Operation(summary = "get list of roles with pagination and filtering")
    @GetMapping(value = "/grid")
    public ResponseEntity<?> getRoles(RoleSearchCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(new ResponseResult("", roleService.getRoles(criteria, pageable)), HttpStatus.OK);
    }

    @Operation(summary = "get a role by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable Long id) {
        return new ResponseEntity<>(new ResponseResult("", roleService.getRole(id)), HttpStatus.OK);
    }

    @Operation(summary = "delete a role by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
