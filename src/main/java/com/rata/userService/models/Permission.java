package com.rata.userService.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String code;
    @ManyToOne
    private Permission parent;
    @ManyToOne
    private Permission prerequisite;
    @OneToMany(mappedBy = "parent")
    private Set<Permission> childes;
    private String url;

}
