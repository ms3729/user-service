package com.rata.userService.models.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@RedisHash("BankCache")
public class BankCache {

    @Id
    private int id;
    private String name;
    private String code;
    private String abbreviation;
}
