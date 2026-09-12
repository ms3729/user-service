package com.rata.userService.models;

import com.rata.userService.models.party.Party;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    @OneToOne(mappedBy = "user")
    @JoinColumn(name = "party_id")
    private Party party;
    @Builder.Default
    private boolean admin = false;
    @Builder.Default
    private boolean superAdmin = false;
    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles;
    @OneToMany(mappedBy = "user")
    private Set<UserUnit> units;
    @OneToMany(mappedBy = "user")
    private Set<UserApplication> applications;
}
