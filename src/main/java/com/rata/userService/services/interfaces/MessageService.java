package com.rata.userService.services.interfaces;

import com.rata.userService.dto.MessageDTO;

public interface MessageService {

    void sendChangePermissionNotification(String username);

    void sendSms(MessageDTO message);
}
