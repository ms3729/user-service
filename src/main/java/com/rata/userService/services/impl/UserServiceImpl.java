package com.rata.userService.services.impl;


import com.rata.userService.config.Utils;
import com.rata.userService.dto.MessageDTO;
import com.rata.userService.enums.MessageLevel;
import com.rata.userService.enums.MessageType;
import com.rata.userService.errorHandling.DuplicateResourceException;
import com.rata.userService.models.Application;
import com.rata.userService.models.Permission;
import com.rata.userService.models.Role;
import com.rata.userService.models.User;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.UserProfileResponse;
import com.rata.userService.repositories.mysql.UserRepository;
import com.rata.userService.services.interfaces.MessageService;
import com.rata.userService.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;

    @Transactional
    @Override
    public User createUserForParty(Party party) {
        // بررسی تکراری نبودن
        if (userRepository.existsByUsername(party.getNationalCode())) {
            throw new DuplicateResourceException("نام کاربری تکراری است: " + party.getNationalCode());
        }

        // ساخت کاربر
        User user = User.builder()
                .username(party.getNationalCode())
                .password(passwordEncoder.encode(party.getPrimaryMobile()))
                .party(party)
                .build();

        return userRepository.save(user);
    }


    @Override
    public void changePassword(String username) {
        changePassword(userRepository.findByUsername(username));
    }

    @Override
    public void changePassword(long id) {
        changePassword(userRepository.findById(id));
    }

    @Override
    public void changePassword(long id, String password) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
        }
    }

    private void changePassword(Optional<User> user) {
        if (user.isPresent()) {
            String password = String.valueOf(Utils.generateRandomNumber());
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
            MessageDTO message = new MessageDTO();
            message.setTemplateId(825579);
            message.setChannel("sms");
            message.setDataType("info");
            message.setLevel(MessageLevel.PRIVATE.toString());
            message.setType(MessageType.info.name());
            message.setTarget(user.get().getParty().getPrimaryMobile());
            Map<String, String> data = new HashMap<>();
            data.put("password", password);
            message.setData(data);
            messageService.sendSms(message);
        }
    }

    @Override
    public void setUsersPassword() {
        List<User> userList = userRepository.findAllWithoutPassword();
        if (!CollectionUtils.isEmpty(userList)) {
            userList.forEach(user -> user.setPassword(passwordEncoder.encode(user.getUsername())));
            userRepository.saveAll(userList);
        }
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> find(long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean userExistsForParty(long partyId) {
        return userRepository.findByPartyId(partyId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        Party party = user.getParty();

        // Build identifiers list
        List<UserProfileResponse.IdentifierInfo> identifiers = new ArrayList<>();
        if (party != null && party.getIdentifiers() != null) {
            identifiers = party.getIdentifiers().stream()
                    .map(pi -> new UserProfileResponse.IdentifierInfo(
                            pi.getId(),
                            pi.getIdentifierType().name(),
                            pi.getValue(),
                            pi.isVerified()
                    ))
                    .collect(Collectors.toList());
        }

        // Build contacts list
        List<UserProfileResponse.ContactInfo> contacts = new ArrayList<>();
        if (party != null && party.getContacts() != null) {
            contacts = party.getContacts().stream()
                    .map(pc -> new UserProfileResponse.ContactInfo(
                            pc.getId(),
                            pc.getContactType().name(),
                            pc.getValue(),
                            pc.isPrimary(),
                            pc.isVerified()
                    ))
                    .collect(Collectors.toList());
        }

        // Build roles list
        List<UserProfileResponse.RoleInfo> roles = new ArrayList<>();
        if (user.getUserRoles() != null) {
            roles = user.getUserRoles().stream()
                    .map(ur -> {
                        Role role = ur.getRole();
                        return new UserProfileResponse.RoleInfo(
                                role != null ? role.getId() : null,
                                role != null ? role.getName() : null,
                                role != null ? role.getCode() : null,
                                ur.getOrganization() != null ? ur.getOrganization().getPartyId() : null,
                                ur.getOrganization() != null && ur.getOrganization().getParty() != null
                                        ? ur.getOrganization().getParty().getDisplayName()
                                        : null
                        );
                    })
                    .collect(Collectors.toList());
        }

        // Build permissions list from UserApplication -> Application and organizationId
        List<UserProfileResponse.PermissionInfo> permissions = new ArrayList<>();
        if (user.getApplications() != null) {
            permissions = user.getApplications().stream()
                    .flatMap(ua -> {
                        List<UserProfileResponse.PermissionInfo> permList = new ArrayList<>();
                        Application app = ua.getApplication();
                        Long orgId = ua.getOrganization() != null ? ua.getOrganization().getPartyId() : null;

                        // Get all permissions from roles assigned to this user
                        if (user.getUserRoles() != null) {
                            user.getUserRoles().forEach(ur -> {
                                if (ur.getRole() != null && ur.getRole().getPermissions() != null) {
                                    ur.getRole().getPermissions().forEach(rp -> {
                                        Permission permission = rp.getPermission();
                                        if (permission != null) {
                                            permList.add(new UserProfileResponse.PermissionInfo(
                                                    app != null ? app.getCode() : null,
                                                    permission.getCode(),
                                                    permission.getUrl(),
                                                    orgId
                                            ));
                                        }
                                    });
                                }
                            });
                        }
                        return permList.stream();
                    })
                    .collect(Collectors.toList());
        }

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                party != null ? party.getDisplayName() : null,
                party != null ? party.getNationalCode() : null,
                party != null ? party.getPersonnelCode() : null,
                party != null ? party.getAvatarUrl() : null,
                roles,
                permissions,
                identifiers,
                contacts
        );
    }

}