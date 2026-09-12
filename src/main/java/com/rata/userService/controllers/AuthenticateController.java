package com.rata.userService.controllers;

import com.rata.userService.config.authentication.AuthRequest;
import com.rata.userService.config.authentication.JwtService;
import com.rata.userService.config.authentication.UserValidate;
import com.rata.userService.dto.ForgetPasswordDTo;
import com.rata.userService.models.User;
import com.rata.userService.records.ResponseResult;
import com.rata.userService.records.TokenRecord;
import com.rata.userService.services.interfaces.UserService;
import com.rata.userService.services.interfaces.VerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/auth")
@Tag(name = "Authentication APIs")
@RequiredArgsConstructor
public class AuthenticateController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final VerificationService verificationService;
    private final Environment env;


    @PostMapping(value = "/user-validate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody UserValidate userValidate) {
        boolean validateSms = verificationService.checkVerificationCode(userValidate.getIdentifierCode(), userValidate.getSmsCode(), userValidate.getUsername());
        if (validateSms) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            return ResponseEntity.ok(new ResponseResult("", jwtService.generateToken(roles, userValidate.getUsername())));
        } else {
            return new ResponseEntity<>(new ResponseResult(env.getProperty("error.invalid_verification_code"), null), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "/login2", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateAndGetToken2(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            return ResponseEntity.ok(new ResponseResult("", jwtService.generateToken(roles, authRequest.getUsername())));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            Optional<User> user = userService.findByUserName(authRequest.getUsername());
            if (user.isEmpty()) {
                return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
            } else {
                String verificationCode = verificationService.checkValidityResendSms(authRequest.getUsername());
                if (verificationCode != null) {
                    return new ResponseEntity<>(new ResponseResult(env.getProperty("error.resend_verification_send_timeout"), verificationCode), HttpStatus.TOO_MANY_REQUESTS);
                }
                return new ResponseEntity<>(new ResponseResult("", verificationService.saveLogin(user.get())), HttpStatus.CREATED);
            }
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        TokenRecord newToken = jwtService.doGenerateRefreshToken(token);
        if (newToken != null) {
            return ResponseEntity.ok(new ResponseResult("", newToken));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @GetMapping(value = "/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) {
        String validated = jwtService.validateExternalToken(token);
        if (!validated.equals("null")) {
            return ResponseEntity.ok().body(validated);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping(value = "/forget-password")
    public ResponseEntity<?> restPassword(@RequestBody ForgetPasswordDTo dto) {
        userService.changePassword(dto.getUsername());
        return ResponseEntity.ok(new ResponseResult("", ""));
    }

}
