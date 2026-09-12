package com.rata.userService.models.party;

import com.rata.userService.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"party_id", "lang"})})
public class OrganizationTranslation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Party party;
    @Column(length = 3)
    private String lang = "fa";
    private String legalName;
    private String tradeName;
    private String registrationName;

}
