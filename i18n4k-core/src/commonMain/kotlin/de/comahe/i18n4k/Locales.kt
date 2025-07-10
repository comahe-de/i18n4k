package de.comahe.i18n4k

/** A set of default [Locale] instances */
object Locales {

    val ENGLISH: Locale = createLocale("en")
    val FRENCH: Locale = createLocale("fr")
    val GERMAN: Locale = createLocale("de")
    val SPANISH: Locale = createLocale("es")
    val ITALIAN: Locale = createLocale("it")
    val JAPANESE: Locale = createLocale("ja")
    val KOREAN: Locale = createLocale("ko")
    val CHINESE: Locale = createLocale("zh")
    val SIMPLIFIED_CHINESE: Locale = createLocale("zh", null,"CN")
    val TRADITIONAL_CHINESE: Locale = createLocale("zh",null, "TW")
    val FRANCE: Locale = createLocale("fr", null, "FR")
    val GERMANY: Locale = createLocale("de", null, "DE")
    val SPAIN: Locale = createLocale("es", null, "ES")
    val ITALY: Locale = createLocale("it", null, "IT")
    val JAPAN: Locale = createLocale("ja", null, "JP")
    val KOREA: Locale = createLocale("ko", null, "KR")
    val CHINA: Locale = SIMPLIFIED_CHINESE
    val PRC: Locale = SIMPLIFIED_CHINESE
    val TAIWAN: Locale = TRADITIONAL_CHINESE
    val UK: Locale = createLocale("en", null, "GB")
    val US: Locale = createLocale("en", null, "US")
    val CANADA: Locale = createLocale("en", null, "CA")
    val CANADA_FRENCH: Locale = createLocale("fr", null, "CA")
}