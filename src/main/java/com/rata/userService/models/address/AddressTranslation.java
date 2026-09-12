package com.rata.userService.models.address;

import com.rata.userService.models.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_translations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_address_translation",
                        columnNames = {"address_id", "lang"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressTranslation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Address address;
    @Column(length = 3)
    private String lang = "fa";
    private String addressLine;
    private String cityName;
    private String stateName;
    private String countryName;

}