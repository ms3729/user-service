package com.rata.userService.config.authentication;

import com.rata.userService.models.User;
import com.rata.userService.services.interfaces.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserService userService;

    public UserInfoService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public FlmUserDetails loadUserByUsername(String username) {
        Optional<User> userDetail = userService.findByUserName(username);
        if (userDetail.isEmpty()) {
            throw new UsernameNotFoundException("error.username_not_found");
        }
        if (!userDetail.get().isEnabled()) {
            throw new IllegalArgumentException("error.user_inactive");
        }
        return new FlmUserDetails(userDetail.get());
    }


} 