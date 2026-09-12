package com.rata.userService.models.party;

import com.rata.userService.enums.RelationType;
import com.rata.userService.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"organization_id", "party_id","relation_type"})})
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Organization organization;
    @ManyToOne
    private Party party;
    @Enumerated(EnumType.STRING)
    private RelationType relationType;

    private LocalDate startDate;
    private LocalDate endDate;

}
