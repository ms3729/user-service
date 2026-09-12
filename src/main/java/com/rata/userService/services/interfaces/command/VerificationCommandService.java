package com.rata.userService.services.interfaces.command;

import com.rata.userService.dto.VerificationDTO;
import com.rata.userService.models.User;

public interface VerificationCommandService{

    String save(VerificationDTO dto);

    String saveLogin(User user);

}
