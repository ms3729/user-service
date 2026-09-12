package com.rata.userService.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String code;
    @Lob
    private String icon;
    private String url;
    @Lob
    private String description;
    private String gradient;
    @OneToMany
    private Set<UserApplication> users;

}
