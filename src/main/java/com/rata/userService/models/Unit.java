package com.rata.userService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Unit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String code;
    @ManyToOne
    private User manager;
    @OneToMany(mappedBy = "unit")
    private Set<UserUnit> users;
}
