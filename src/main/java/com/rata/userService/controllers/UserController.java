package com.rata.userService.controllers;

import com.rata.userService.config.authentication.JwtService;
import com.rata.userService.models.User;
import com.rata.userService.models.docs.UserSessionHistory;
import com.rata.userService.records.ResponseResult;
import com.rata.userService.records.UserProfileResponse;
import com.rata.userService.services.interfaces.UserService;
import com.rata.userService.services.interfaces.UserSessionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/usrs")
@Tag(name = "User APIs")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserSessionHistoryService userSessionHistoryService;


    @Operation(summary = "get list of user history")
    @GetMapping("/login-history")
    public ResponseEntity<?> getUserLoginHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUserName(jwtService.extractUsername((String) authentication.getPrincipal()));
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<UserSessionHistory> histories = userSessionHistoryService.findAllByUsername(user.get().getUsername());
        if (CollectionUtils.isEmpty(histories)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseResult("", histories), HttpStatus.OK);
    }

    @Operation(summary = "grid of users with pagination and filter")
    @GetMapping(value = "/set-password")
    public ResponseEntity<?> setUsersPassword() {
        userService.setUsersPassword();
        return new ResponseEntity<>(new ResponseResult("", null), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/reset-password")
    public ResponseEntity<?> restPassword(@PathVariable long id) {
        userService.changePassword(id);
        return ResponseEntity.ok(new ResponseResult("", ""));
    }

    @PostMapping(value = "/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable long id, @RequestParam String password) {
        userService.changePassword(id, password);
        return ResponseEntity.ok(new ResponseResult("", ""));
    }

    @Operation(summary = "get user profile with roles, permissions, identifiers and contacts")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = jwtService.extractUsername((String) authentication.getPrincipal());
        UserProfileResponse profile = userService.getUserProfile(username);
        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new ResponseResult("", profile), HttpStatus.OK);
    }

}
