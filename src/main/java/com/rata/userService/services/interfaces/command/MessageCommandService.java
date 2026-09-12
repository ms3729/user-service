package com.rata.userService.services.interfaces.command;

import com.rata.userService.dto.MessageDTO;

public interface MessageCommandService {

    void sendChangePermissionNotification(String username);

    void sendSms(MessageDTO message);
}
