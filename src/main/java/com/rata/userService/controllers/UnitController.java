package com.rata.userService.controllers;

import com.rata.userService.config.authentication.JwtService;
import com.rata.userService.models.User;
import com.rata.userService.records.ResponseResult;
import com.rata.userService.records.UnitRecord;
import com.rata.userService.services.interfaces.UserService;
import com.rata.userService.services.interfaces.query.UnitQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/units")
@Tag(name = "Unit APIs")
@RequiredArgsConstructor
public class UnitController {

    private final UserService userService;
    private final UnitQueryService unitQueryService;
    private final JwtService jwtService;

    @GetMapping("/user-units")
    public ResponseEntity<?> getUserUnits() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByUserName(jwtService.extractUsername((String) authentication.getPrincipal()));
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UnitRecord> dtoList = unitQueryService.findAllByUserId(user.get().getId());
        return new ResponseEntity<>(new ResponseResult("", dtoList), HttpStatus.OK);
    }


}
