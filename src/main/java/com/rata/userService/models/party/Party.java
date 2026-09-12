package com.rata.userService.models.party;

import com.rata.userService.enums.ContactType;
import com.rata.userService.enums.PartyType;
import com.rata.userService.models.BankInfo;
import com.rata.userService.models.BaseEntity;
import com.rata.userService.models.User;
import com.rata.userService.models.address.Address;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 11, nullable = false)
    @Size(min = 10, max = 11)
    private String nationalCode;
    @Builder.Default
    private String defaultLanguage = "fa";
    @Enumerated(EnumType.STRING)
    private PartyType partyType;
    private String personnelCode;
    private Integer detailedCode;
    @Column(length = 500)
    private String avatarUrl;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PartyIdentifier> identifiers = new HashSet<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PartyContact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Builder.Default
    private Set<BankInfo> bankInfos = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Builder.Default
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Builder.Default
    private Set<Membership> memberships = new HashSet<>();

    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private Person person;

    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private Organization organization;
    @OneToOne
    private User user;

    // ✅ ترجمه‌ها در Party باشند
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PersonTranslation> personTranslations = new HashSet<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OrganizationTranslation> organizationTranslations = new HashSet<>();


    // Helper methods
    public void addIdentifier(PartyIdentifier identifier) {
        identifiers.add(identifier);
        identifier.setParty(this);
    }

    public void removeIdentifier(PartyIdentifier identifier) {
        identifiers.remove(identifier);
        identifier.setParty(null);
    }

    public void addContact(PartyContact contact) {
        contacts.add(contact);
        contact.setParty(this);
    }

    public void removeContact(PartyContact contact) {
        contacts.remove(contact);
        contact.setParty(null);
    }

    public void addBankInfo(BankInfo bankInfo) {
        bankInfos.add(bankInfo);
        bankInfo.setParty(this);
    }

    public void removeBankInfo(BankInfo bankInfo) {
        bankInfos.remove(bankInfo);
        bankInfo.setParty(null);
    }
    public void addPersonTranslation(PersonTranslation translation) {
        personTranslations.add(translation);
        translation.setParty(this);
    }

    public void removePersonTranslation(PersonTranslation translation) {
        personTranslations.remove(translation);
        translation.setParty(null);
    }

    public void addOrganizationTranslation(OrganizationTranslation translation) {
        organizationTranslations.add(translation);
        translation.setParty(this);
    }

    public void removeOrganizationTranslation(OrganizationTranslation translation) {
        organizationTranslations.remove(translation);
        translation.setParty(null);
    }


    public void addMembership(Membership membership) {
        memberships.add(membership);
        membership.setParty(this);
    }

    public void removeMembership(Membership membership) {
        memberships.remove(membership);
        membership.setParty(null);
    }

    // Utility methods
    public boolean isPerson() {
        return partyType == PartyType.PERSON;
    }

    public boolean isOrganization() {
        return partyType == PartyType.ORGANIZATION;
    }

    public String getPrimaryEmail() {
        return contacts.stream()
                .filter(c -> c.getContactType() == ContactType.EMAIL && c.isPrimary())
                .map(PartyContact::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getPrimaryMobile() {
        return contacts.stream()
                .filter(c -> c.getContactType() == ContactType.MOBILE && c.isPrimary())
                .map(PartyContact::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getPrimaryPhone() {
        return contacts.stream()
                .filter(c -> c.getContactType() == ContactType.PHONE && c.isPrimary())
                .map(PartyContact::getValue)
                .findFirst()
                .orElse(null);
    }

    public Address getDefaultAddress() {
        return addresses.stream().filter(Objects::nonNull)
                .filter(Address::isDefault)
                .findFirst()
                .orElse(null);
    }

    public String getDisplayName() {
        return getDisplayName(defaultLanguage);
    }


    public String getDisplayName(String requestedLanguage) {
        if (partyType == PartyType.PERSON && person != null) {
            return person.getFullName(requestedLanguage);
        }

        if (partyType == PartyType.ORGANIZATION && organization != null) {
            return organization.getDisplayName(requestedLanguage);
        }

        return null;
    }

}
