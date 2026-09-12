package com.rata.userService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Verification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String identifyCode;
    private String mobile;
    @ManyToOne
    private User user;
    private Date sendDate;
    private String code;
    private String type;
    private String data;
}
