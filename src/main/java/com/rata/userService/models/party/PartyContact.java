package com.rata.userService.models.party;

import com.rata.userService.enums.ContactType;
import com.rata.userService.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "party_contacts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_party_contact",
                        columnNames = {"party_id", "contact_type", "value"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyContact extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "is_primary", nullable = false)
    @Builder.Default
    private boolean isPrimary = false;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    private boolean verified = false;


}