package com.rata.userService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String icon;
    private String url;
    private String component;
    private String translationKey;
    @ManyToOne
    private Permission permission;
    @ManyToOne
    private Module module;
    @ManyToOne
    private Menu parent;
    @OneToMany(mappedBy = "parent")
    private List<Menu> childes;
}
