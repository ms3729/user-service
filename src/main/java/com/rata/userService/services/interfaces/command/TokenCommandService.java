package com.rata.userService.services.interfaces.command;

import com.rata.userService.models.Token;

import java.util.List;


public interface TokenCommandService {

    Token save(Token token);

    void deleteById(String id);

    List<Token> findAll();

    Token findById(String id);


}
