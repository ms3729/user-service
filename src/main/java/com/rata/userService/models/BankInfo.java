package com.rata.userService.models;

import com.rata.userService.models.party.Party;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String iban;
    private Integer bankId;
    private String bankAccountNumber;
    private String bankCardNumber;
    private String bankAccountOwner;
    private boolean active = true;
    private boolean fund;
    @ManyToOne
    private Party party;

    public BankInfo(Integer bankId, String iban, String bankAccountNumber, String bankCardNumber, String bankAccountOwner) {
        if (bankId == null) {
            throw new IllegalArgumentException("error.bank_is_not_selected");
        }
        this.bankId = bankId;
        if (iban == null && bankAccountNumber == null && bankCardNumber == null) {
            throw new IllegalArgumentException("error.fill_at_least_one_type_of_bank_info");
        }
        if (iban != null && iban.length() != 24) {
            throw new IllegalArgumentException("error.invalid_iban_length");
        }
        this.iban = iban;
        this.bankAccountNumber = bankAccountNumber;
        if (bankCardNumber != null && bankCardNumber.length() != 16) {
            throw new IllegalArgumentException("error.invalid_bank_card_length");
        }
        this.bankCardNumber = bankCardNumber;
        this.bankAccountOwner = bankAccountOwner;
    }
}
