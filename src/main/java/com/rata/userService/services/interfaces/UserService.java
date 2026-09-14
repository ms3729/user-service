package com.rata.userService.services.interfaces;

import com.rata.userService.models.User;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.UserPermissionsMenusResponse;
import com.rata.userService.records.UserProfileResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {

    @Transactional
    User createUserForParty(Party party);

    void changePassword(String username);

    void changePassword(long userId);

    void changePassword(long userId, String password);

    void setUsersPassword();

    Optional<User> findByUserName(String username);

    Optional<User> find(long id);

    boolean userExistsForParty(long partyId);

    UserProfileResponse getUserProfile(String username);

    UserPermissionsMenusResponse getUserPermissionsAndMenus(String username);
}
