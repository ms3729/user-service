package com.rata.userService.models.party;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "party_external_refs",
       uniqueConstraints = {
           @UniqueConstraint(
               name = "uq_party_external_ref",
               columnNames = {"source_service", "source_entity_id"}
           )
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyExternalRef {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(name = "source_service", nullable = false)
    private String sourceService;

    @Column(name = "source_entity_type", nullable = false)
    private String sourceEntityType;

    @Column(name = "source_entity_id", nullable = false)
    private String sourceEntityId;

    @CreatedDate
    private OffsetDateTime createdAt;
}