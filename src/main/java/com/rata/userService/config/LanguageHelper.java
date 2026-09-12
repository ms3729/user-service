package com.rata.userService.config;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class LanguageHelper {

    public static final String SYSTEM_DEFAULT = "fa";

    private LanguageHelper() {
    }

    public static String normalize(String languageTag) {
        if (languageTag == null || languageTag.isBlank()) {
            return SYSTEM_DEFAULT;
        }

        Locale locale = Locale.forLanguageTag(languageTag.trim());

        if (locale == null || locale.toLanguageTag().equals("und")) {
            return languageTag.trim().toLowerCase(Locale.ROOT);
        }

        return locale.toLanguageTag();
    }

    public static List<String> fallbackChain(
            String requestedLanguage,
            String entityDefaultLanguage
    ) {
        LinkedHashSet<String> chain = new LinkedHashSet<>();

        addLanguage(chain, requestedLanguage);
        addLanguage(chain, entityDefaultLanguage);

        chain.add(SYSTEM_DEFAULT);

        return List.copyOf(chain);
    }

    private static void addLanguage(Set<String> chain, String languageTag) {
        if (languageTag == null || languageTag.isBlank()) {
            return;
        }

        Locale locale = Locale.forLanguageTag(languageTag.trim());

        if (locale != null && !locale.toLanguageTag().equals("und")) {
            chain.add(locale.toLanguageTag());

            if (!locale.getLanguage().isBlank()) {
                chain.add(locale.getLanguage().toLowerCase(Locale.ROOT));
            }
        } else {
            chain.add(languageTag.trim().toLowerCase(Locale.ROOT));
        }
    }
}