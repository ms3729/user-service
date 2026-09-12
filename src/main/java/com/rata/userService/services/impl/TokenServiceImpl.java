package com.rata.userService.services.impl;

import com.rata.userService.models.Token;
import com.rata.userService.repositories.redis.db0.TokenRepository;
import com.rata.userService.services.interfaces.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.rata.userService.config.redis.RedisConstants.TOKEN;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {


    private final TokenRepository tokenRepository;


    @CacheEvict(value = TOKEN, allEntries = true)
    @CachePut(value = TOKEN, key = "#token.id", unless = "#token != null")
    public Token save(Token token) {
        try {
            return tokenRepository.save(token);
        } catch (Exception e) {
            return null;
        }
    }

    @CacheEvict(value = TOKEN, condition = "#id != null", allEntries = true)
    public void deleteById(String id) {
        tokenRepository.deleteById(id);
    }

    @Cacheable(value = TOKEN)
    public List<Token> findAll() {
        return (List<Token>) tokenRepository.findAll();
    }

    @Cacheable(value = TOKEN, key = "#id", condition = "#id != null")
    public Token findById(String id) {
        try {
            Optional<Token> token = tokenRepository.findById(id);
            return token.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }


}
