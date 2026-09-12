package com.rata.userService.models.docs;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;

@Data
@Document("userSessionHistory")
public class UserSessionHistory {

    @Id
    @Indexed
    private String id;
    private Date loginDate;
    private String ip;
    private String systemInfo;
    private String username;

}
