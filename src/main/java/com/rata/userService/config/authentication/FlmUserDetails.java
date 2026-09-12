package com.rata.userService.config.authentication;

import com.rata.userService.models.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlmUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities;
    @Getter
    private final long organizationId;

    public FlmUserDetails(User user) {
        password = user.getPassword();
        username = user.getUsername();
        enabled = user.isEnabled();
        organizationId = user.getParty().getOrganization().getPartyId();
        authorities = user.getUserRoles().stream().filter(Objects::nonNull)
                .filter(ur -> ur.getRole().isSystemRole() && ur.getRole().getCode() != null)
                .map(ur -> new SimpleGrantedAuthority(ur.getRole().getCode())).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}