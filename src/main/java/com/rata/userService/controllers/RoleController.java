package com.rata.userService.controllers;

import com.rata.userService.records.CreateRoleRequest;
import com.rata.userService.records.ResponseResult;
import com.rata.userService.records.RoleResponse;
import com.rata.userService.records.UpdateRoleRequest;
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
    public ResponseEntity<RoleResponse> createRole(
            @Valid @RequestBody CreateRoleRequest request
    ) {
        RoleResponse response = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "update an existing role")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
        RoleResponse response = roleService.updateRole(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "get list of roles with pagination")
    @GetMapping(value = "/grid")
    public ResponseEntity<?> getRoles(Pageable pageable) {
        return new ResponseEntity<>(new ResponseResult("", roleService.getRoles(pageable)), HttpStatus.OK);
    }

    @Operation(summary = "get a role by id")
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable Long id) {
        RoleResponse response = roleService.getRole(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "delete a role by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
