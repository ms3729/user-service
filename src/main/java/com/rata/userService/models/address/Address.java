package com.rata.userService.models.address;

import com.rata.userService.enums.AddressType;
import com.rata.userService.models.BaseEntity;
import com.rata.userService.models.party.Party;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Party party;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    private String postalCode;
    private boolean isDefault = false;
    private Integer cityId;
    private Integer stateId;
    private Integer countryId;
    private String latitude;
    private String longitude;
    @OneToMany(mappedBy = "address")
    private Set<AddressTranslation> translations = new HashSet<>();


    public AddressTranslation getDefaultTranslation(String lang) {
        return translations.stream().filter(Objects::nonNull)
                .filter(c -> c.getLang().equals(lang))
                .findFirst()
                .orElse(null);
    }
}
