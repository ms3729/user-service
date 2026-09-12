package com.rata.userService.models.party;

import com.rata.userService.config.LanguageHelper;
import com.rata.userService.enums.EducationLevel;
import com.rata.userService.models.BaseEntity;
import com.rata.userService.services.impl.TranslationResolver;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person extends BaseEntity {

    @Id
    @Column(name = "party_id")
    private Long partyId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "party_id")
    private Party party;
    private LocalDate birthDate;
    private Boolean gender;
    private String description;
    private EducationLevel educationLevel;


    // ==================== Helper Methods ====================

    /**
     * نام کامل را با استفاده از defaultLanguage موجود در Party برمی‌گرداند
     */
    public String getFullName() {
        return getFullName(party != null ? party.getDefaultLanguage() : null);
    }

    /**
     * نام کامل را با زبان مشخص‌شده برمی‌گرداند
     */
    public String getFullName(String requestedLanguage) {
        PersonTranslation translation = getTranslation(requestedLanguage);

        if (translation == null) {
            return null;
        }

        return buildFullName(translation);
    }

    /**
     * نام کوچک را برمی‌گرداند (با fallback)
     */
    public String getFirstName() {
        return getFirstName(party != null ? party.getDefaultLanguage() : null);
    }

    public String getFirstName(String requestedLanguage) {
        PersonTranslation translation = getTranslation(requestedLanguage);
        return translation != null ? translation.getFirstname() : null;
    }

    /**
     * نام خانوادگی را برمی‌گرداند (با fallback)
     */
    public String getLastName() {
        return getLastName(party != null ? party.getDefaultLanguage() : null);
    }

    public String getLastName(String requestedLanguage) {
        PersonTranslation translation = getTranslation(requestedLanguage);
        return translation != null ? translation.getLastname() : null;
    }

    /**
     * ترجمه را بر اساس زبان درخواستی و defaultLanguage پیدا می‌کند
     */
    public PersonTranslation getTranslation(String requestedLanguage) {
        if (party == null || party.getPersonTranslations() == null) {
            return null;
        }

        return TranslationResolver.resolve(
                party.getPersonTranslations(),
                requestedLanguage,
                party.getDefaultLanguage(),
                PersonTranslation::getLang
        );
    }

    /**
     * بررسی می‌کند که آیا ترجمه‌ای برای زبان مشخص وجود دارد یا خیر
     */
    public boolean hasTranslation(String languageCode) {
        if (party == null || party.getPersonTranslations() == null || languageCode == null) {
            return false;
        }
        String normalized = LanguageHelper.normalize(languageCode);
        return party.getPersonTranslations().stream()
                .anyMatch(t -> normalized.equalsIgnoreCase(t.getLang()));
    }

    /**
     * تعداد زبان‌های موجود برای این شخص
     */
    public int getTranslationCount() {
        if (party == null || party.getPersonTranslations() == null) {
            return 0;
        }
        return party.getPersonTranslations().size();
    }

    // ==================== Private Helpers ====================

    private String buildFullName(PersonTranslation translation) {

        // در غیر این صورت از firstName + middleName + lastName بساز
        StringBuilder sb = new StringBuilder();

        if (translation.getFirstname() != null && !translation.getFirstname().isBlank()) {
            sb.append(translation.getFirstname().trim());
        }

        if (translation.getLastname() != null && !translation.getLastname().isBlank()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(translation.getLastname().trim());
        }

        return sb.length() > 0 ? sb.toString() : null;
    }

}
