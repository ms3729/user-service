package com.rata.userService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String code;
    private boolean systemRole = false;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<UserRole> users;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<RolePermission> permissions;
    @ManyToOne
    private Application app;
}
