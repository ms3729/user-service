package com.rata.userService.models;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@RedisHash(value = "token")
public class Token implements Serializable {

    @Id
    private String id;
    private String access;
    private String refresh;
    private Date expiration;

}
