package com.rata.userService.controllers;

import com.rata.userService.dto.PermissionDTO;
import com.rata.userService.records.ResponseResult;
import com.rata.userService.services.interfaces.query.PermissionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/permissions")
@Tag(name = "Permission APIs")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionQueryService permissionQueryService;

    @Operation(summary = "get list of permissions")
    @GetMapping("/")
    public ResponseEntity<?> getPermissionsList() {
        List<PermissionDTO> list = permissionQueryService.findAllByEnabledStatus(true);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseResult("",list), HttpStatus.OK);
    }

    @Operation(summary = "get a permission acquire id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPermission(@PathVariable long id) {
        Optional<PermissionDTO> dto = permissionQueryService.findById(id);
        if (dto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseResult("", dto.get()), HttpStatus.OK);
    }

}
