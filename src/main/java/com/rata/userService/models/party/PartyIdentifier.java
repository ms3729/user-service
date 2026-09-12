package com.rata.userService.models.party;

import com.rata.userService.enums.IdentifierType;
import com.rata.userService.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "party_identifiers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_party_identifier",
                        columnNames = {"party_id", "identifier_type", "country_code", "value"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyIdentifier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(name = "identifier_type", nullable = false)
    private IdentifierType identifierType;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    private boolean verified = false;

}