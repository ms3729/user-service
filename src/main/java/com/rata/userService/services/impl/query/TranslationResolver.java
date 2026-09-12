package com.rata.userService.services.impl.query;

import com.rata.userService.config.LanguageHelper;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public final class TranslationResolver {

    private TranslationResolver() {
    }

    /**
     * پیدا کردن بهترین ترجمه بر اساس زبان درخواستی و fallback
     *
     * @param translations       لیست ترجمه‌ها
     * @param requestedLanguage  زبان درخواستی
     * @param defaultLanguage    زبان پیش‌فرض Party
     * @param languageExtractor  تابعی که زبان را از ترجمه استخراج می‌کند
     * @param <T>                نوع ترجمه
     * @return بهترین ترجمه موجود، یا null اگر هیچ ترجمه‌ای نباشد
     */
    public static <T> T resolve(
            Collection<T> translations,
            String requestedLanguage,
            String defaultLanguage,
            Function<T, String> languageExtractor
    ) {
        if (translations == null || translations.isEmpty()) {
            return null;
        }

        List<String> chain = LanguageHelper.fallbackChain(
                requestedLanguage,
                defaultLanguage
        );

        // اول بر اساس زنجیره fallback
        for (String lang : chain) {
            for (T translation : translations) {
                String translationLang = languageExtractor.apply(translation);
                if (lang.equalsIgnoreCase(translationLang)) {
                    return translation;
                }
            }
        }

        // دوم: تطابق base language (مثلا fa-IR با fa)
        for (String lang : chain) {
            String baseLang = getBaseLanguage(lang);
            if (baseLang == null) continue;

            for (T translation : translations) {
                String translationLang = languageExtractor.apply(translation);
                if (baseLang.equalsIgnoreCase(getBaseLanguage(translationLang))) {
                    return translation;
                }
            }
        }

        // در نهایت اولین ترجمه موجود
        return translations.iterator().next();
    }

    private static String getBaseLanguage(String languageTag) {
        if (languageTag == null || languageTag.isBlank()) {
            return null;
        }
        Locale locale = Locale.forLanguageTag(languageTag.trim());
        if (locale == null || locale.toLanguageTag().equals("und")) {
            return languageTag.trim().toLowerCase(Locale.ROOT);
        }
        return locale.getLanguage().isBlank() ? null : locale.getLanguage().toLowerCase(Locale.ROOT);
    }
}