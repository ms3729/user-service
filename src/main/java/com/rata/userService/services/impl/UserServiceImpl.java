package com.rata.userService.services.impl;


import com.rata.userService.config.Utils;
import com.rata.userService.dto.MessageDTO;
import com.rata.userService.enums.MessageLevel;
import com.rata.userService.enums.MessageType;
import com.rata.userService.errorHandling.DuplicateResourceException;
import com.rata.userService.models.*;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.UserPermissionsMenusResponse;
import com.rata.userService.records.UserProfileResponse;
import com.rata.userService.repositories.mysql.MenuRepository;
import com.rata.userService.repositories.mysql.PermissionRepository;
import com.rata.userService.repositories.mysql.UserRepository;
import com.rata.userService.services.interfaces.MessageService;
import com.rata.userService.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;
    private final MenuRepository menuRepository;
    private final PermissionRepository permissionRepository;

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
                                role != null ? role.getName() : null
                        );
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
                identifiers,
                contacts
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserPermissionsMenusResponse getUserPermissionsAndMenus(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        Long userId = user.getId();

        // Get permissions for user
        List<Permission> permissions = permissionRepository.findByUserId(userId);
        List<UserPermissionsMenusResponse.PermissionInfo> permissionInfos = permissions.stream()
                .map(p -> new UserPermissionsMenusResponse.PermissionInfo(
                        p.getCode(),
                        p.getUrl() != null ? p.getUrl() : "#"
                ))
                .collect(Collectors.toList());

        // Get menus for user
        List<Menu> menus = menuRepository.findByUserId(userId);
        List<UserPermissionsMenusResponse.MenuInfo> menuInfos = buildMenuTree(menus);

        return new UserPermissionsMenusResponse(permissionInfos, menuInfos);
    }

    private List<UserPermissionsMenusResponse.MenuInfo> buildMenuTree(List<Menu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }

        // Build a map of menu id to MenuInfo for quick lookup
        Map<Long, UserPermissionsMenusResponse.MenuInfo> menuMap = new HashMap<>();
        for (Menu menu : menus) {
            String appCode = null;
            if (menu.getModule() != null && menu.getModule().getApp() != null) {
                appCode = menu.getModule().getApp().getCode();
            }
            
            UserPermissionsMenusResponse.MenuInfo menuInfo = new UserPermissionsMenusResponse.MenuInfo(
                    menu.getId(),
                    menu.getName(),
                    menu.getIcon(),
                    menu.getUrl(),
                    appCode,
                    menu.getComponent(),
                    menu.getTranslationKey(),
                    menu.getPermission() != null ? menu.getPermission().getCode() : null,
                    new ArrayList<>()
            );
            menuMap.put(menu.getId(), menuInfo);
        }

        // Build the tree structure
        List<UserPermissionsMenusResponse.MenuInfo> rootMenus = new ArrayList<>();
        for (Menu menu : menus) {
            UserPermissionsMenusResponse.MenuInfo menuInfo = menuMap.get(menu.getId());
            Menu parent = menu.getParent();
            
            if (parent != null && menuMap.containsKey(parent.getId())) {
                // Add as child to parent
                menuMap.get(parent.getId()).childes().add(menuInfo);
            } else {
                // Root menu
                rootMenus.add(menuInfo);
            }
        }

        return rootMenus;
    }

}