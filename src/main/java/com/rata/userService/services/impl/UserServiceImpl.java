package com.rata.userService.services.impl;


import com.rata.userService.config.Utils;
import com.rata.userService.dto.MessageDTO;
import com.rata.userService.enums.MessageLevel;
import com.rata.userService.enums.MessageType;
import com.rata.userService.errorHandling.DuplicateResourceException;
import com.rata.userService.models.User;
import com.rata.userService.models.party.Party;
import com.rata.userService.repositories.mysql.UserRepository;
import com.rata.userService.services.interfaces.MessageService;
import com.rata.userService.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

}