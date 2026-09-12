package com.rata.userService.models.party;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @Column(name = "party_id")
    private Long partyId;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "party_id")
    private Party party;
    private int approximatePersonnel;
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Membership> memberships = new HashSet<>();
    @ManyToOne
    private BusinessSector businessSector;
    @ManyToOne
    private OwnershipType ownershipType;


    public void addMembership(Membership membership) {
        memberships.add(membership);
        membership.setOrganization(this);
    }

    public void removeMembership(Membership membership) {
        memberships.remove(membership);
        membership.setOrganization(null);
    }

    // Helper method برای دسترسی به ترجمه‌ها از طریق Party
    public String getDisplayName(String requestedLanguage) {
        if (party == null || party.getOrganizationTranslations() == null) {
            return null;
        }
        for (OrganizationTranslation translation : party.getOrganizationTranslations()) {
            if (translation.getLang().equals(requestedLanguage)) {
                return translation.getLegalName();
            }
        }

        // Fallback به اولین ترجمه
        if (!party.getOrganizationTranslations().isEmpty()) {
            OrganizationTranslation first = party.getOrganizationTranslations().iterator().next();
            return first.getLegalName();
        }
        return null;
    }
}
