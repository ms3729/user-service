package com.rata.userService.services.interfaces;

import com.rata.userService.models.Token;

import java.util.List;


public interface TokenService {

    Token save(Token token);

    void deleteById(String id);

    List<Token> findAll();

    Token findById(String id);


}
