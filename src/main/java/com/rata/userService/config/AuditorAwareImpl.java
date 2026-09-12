package com.rata.userService.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of((String) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        } catch (NullPointerException e) {
            return Optional.of("0000000000");
        }
    }

}