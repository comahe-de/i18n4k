@file:Suppress("unused")

package de.comahe.i18n4k

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.collections.immutable.persistentMapOf


/**
 * Property to access [Locale.getLanguage]
 *
 * Direct expected property not possible because `java.util.Locale` should
 * be a actual typealias and getter cannot replace property currently:
 * https://youtrack.jetbrains.com/issue/KT-15620
 */
val Locale.language: String
    get() = this.getLanguage()

/**
 * Property to access [Locale.getCountry]
 *
 */
val Locale.country: String
    get() = this.getCountry()

/**
 * Property to access [Locale.getVariant]
 *
 */
val Locale.variant: String
    get() = this.getVariant()

/** Cache for [lessSpecificLocale] to prevent memory allocations. */
private val lessSpecificLocaleCache = atomic(persistentMapOf<Locale, Locale?>())

/**
 * Creates a locale that is one step less specific than this locale.
 *
 * Returns null if there is no less specific locale.
 *
 * E.g. "de_DE_saxony" would become "de_DE", "de_DE" would become "de" and
 * "de" would become null.
 */
val Locale.lessSpecificLocale: Locale?
    get() {
        if (variant.isEmpty() && country.isEmpty())
            return null

        var result = lessSpecificLocaleCache.value[this]
        if (result === null) {
            result = if (variant.isEmpty())
                Locale(language)
            else
                Locale(language, country)
        }
        lessSpecificLocaleCache.update { it.put(this, result) }

        return result
    }


/** Transforms a languageTag like "en_US_WIN" to a Locale("en","US","WIN") */
fun forLocaleTag(languageTag: String, separator: String = "_"): Locale {
    val underscore1 = languageTag.indexOf(separator)
    if (underscore1 < 0)
        return Locale(languageTag)

    val underscore2 = languageTag.indexOf(separator, underscore1 + 1)
    if (underscore2 < 0)
        return Locale(
            languageTag.substring(0, underscore1),
            languageTag.substring(underscore1 + 1)
        )

    return Locale(
        languageTag.substring(0, underscore1),
        languageTag.substring(underscore1 + 1, underscore2),
        languageTag.substring(underscore2 + 1)
    )
}


fun Locale.toTag(): String {
    if (country.isEmpty())
        return language
    if (variant.isEmpty())
        return language + "_" + country
    return language + "_" + country + "_" + variant
}

/** Returns a name for the locale that is appropriate for display to the user in the language of the locale */
fun Locale.getDisplayNameInLocale(): String {
    val displayLanguage = localeTags.binarySearch(language).let {
        if (it < 0)
            language
        else
            localeDisplayName[it]
    }

    if (country.isEmpty())
        return displayLanguage

    val displayCountry = localeTags.binarySearch(language + "_" + country).let {
        if (it < 0)
            "$displayLanguage ($country)"
        else
            localeDisplayName[it]
    }

    if (variant.isEmpty())
        return displayCountry


    return localeTags.binarySearch(language + "_" + country + "_" + variant).let {
        if (it < 0)
            "${displayCountry.substringBefore(")")},$variant)"
        else
            localeDisplayName[it]
    }
}

/** Locale  */
private val localeTags = arrayListOf(
    "ar",
    "ar_AE",
    "ar_BH",
    "ar_DZ",
    "ar_EG",
    "ar_IQ",
    "ar_JO",
    "ar_KW",
    "ar_LB",
    "ar_LY",
    "ar_MA",
    "ar_OM",
    "ar_QA",
    "ar_SA",
    "ar_SD",
    "ar_SY",
    "ar_TN",
    "ar_YE",
    "be",
    "be_BY",
    "bg",
    "bg_BG",
    "ca",
    "ca_ES",
    "cs",
    "cs_CZ",
    "da",
    "da_DK",
    "de",
    "de_AT",
    "de_CH",
    "de_DE",
    "de_GR",
    "de_LU",
    "el",
    "el_CY",
    "el_GR",
    "en",
    "en_AU",
    "en_CA",
    "en_GB",
    "en_IE",
    "en_IN",
    "en_MT",
    "en_NZ",
    "en_PH",
    "en_SG",
    "en_US",
    "en_ZA",
    "es",
    "es_AR",
    "es_BO",
    "es_CL",
    "es_CO",
    "es_CR",
    "es_CU",
    "es_DO",
    "es_EC",
    "es_ES",
    "es_GT",
    "es_HN",
    "es_MX",
    "es_NI",
    "es_PA",
    "es_PE",
    "es_PR",
    "es_PY",
    "es_SV",
    "es_US",
    "es_UY",
    "es_VE",
    "et",
    "et_EE",
    "fi",
    "fi_FI",
    "fr",
    "fr_BE",
    "fr_CA",
    "fr_CH",
    "fr_FR",
    "fr_LU",
    "ga",
    "ga_IE",
    "hi",
    "hi_IN",
    "hr",
    "hr_HR",
    "hu",
    "hu_HU",
    "in",
    "in_ID",
    "is",
    "is_IS",
    "it",
    "it_CH",
    "it_IT",
    "iw",
    "iw_IL",
    "ja",
    "ja_JP",
    "ko",
    "ko_KR",
    "lt",
    "lt_LT",
    "lv",
    "lv_LV",
    "mk",
    "mk_MK",
    "ms",
    "ms_MY",
    "mt",
    "mt_MT",
    "nl",
    "nl_BE",
    "nl_NL",
    "no",
    "no_NO",
    "no_NO_NY",
    "pl",
    "pl_PL",
    "pt",
    "pt_BR",
    "pt_PT",
    "ro",
    "ro_RO",
    "ru",
    "ru_RU",
    "sk",
    "sk_SK",
    "sl",
    "sl_SI",
    "sq",
    "sq_AL",
    "sr",
    "sr_BA",
    "sr_BA_#Latn",
    "sr_CS",
    "sr_ME",
    "sr_ME_#Latn",
    "sr_RS",
    "sr_RS_#Latn",
    "sr__#Latn",
    "sv",
    "sv_SE",
    "th",
    "th_TH",
    "tr",
    "tr_TR",
    "uk",
    "uk_UA",
    "vi",
    "vi_VN",
    "zh",
    "zh_CN",
    "zh_HK",
    "zh_SG",
    "zh_TW",
)

/** Display names of locales defined in [localeTags] */
private val localeDisplayName = arrayListOf(
    "العربية",
    "العربية (الإمارات)",
    "العربية (البحرين)",
    "العربية (الجزائر)",
    "العربية (مصر)",
    "العربية (العراق)",
    "العربية (الأردن)",
    "العربية (الكويت)",
    "العربية (لبنان)",
    "العربية (ليبيا)",
    "العربية (المغرب)",
    "العربية (سلطنة عمان)",
    "العربية (قطر)",
    "العربية (السعودية)",
    "العربية (السودان)",
    "العربية (سوريا)",
    "العربية (تونس)",
    "العربية (اليمن)",
    "беларускі",
    "беларускі (Беларусь)",
    "български",
    "български (България)",
    "català",
    "català (Espanya)",
    "čeština",
    "čeština (Česká republika)",
    "Dansk",
    "Dansk (Danmark)",
    "Deutsch",
    "Deutsch (Österreich)",
    "Deutsch (Schweiz)",
    "Deutsch (Deutschland)",
    "Deutsch (Griechenland)",
    "Deutsch (Luxemburg)",
    "Ελληνικά",
    "Ελληνικά (Κύπρος)",
    "Ελληνικά (Ελλάδα)",
    "English",
    "English (Australia)",
    "English (Canada)",
    "English (United Kingdom)",
    "English (Ireland)",
    "English (India)",
    "English (Malta)",
    "English (New Zealand)",
    "English (Philippines)",
    "English (Singapore)",
    "English (United States)",
    "English (South Africa)",
    "español",
    "español (Argentina)",
    "español (Bolivia)",
    "español (Chile)",
    "español (Colombia)",
    "español (Costa Rica)",
    "español (Cuba)",
    "español (República Dominicana)",
    "español (Ecuador)",
    "español (España)",
    "español (Guatemala)",
    "español (Honduras)",
    "español (México)",
    "español (Nicaragua)",
    "español (Panamá)",
    "español (Perú)",
    "español (Puerto Rico)",
    "español (Paraguay)",
    "español (El Salvador)",
    "español (Estados Unidos)",
    "español (Uruguay)",
    "español (Venezuela)",
    "Eesti",
    "Eesti (Eesti)",
    "suomi",
    "suomi (Suomi)",
    "français",
    "français (Belgique)",
    "français (Canada)",
    "français (Suisse)",
    "français (France)",
    "français (Luxembourg)",
    "Gaeilge",
    "Gaeilge (Éire)",
    "हिंदी",
    "हिंदी (भारत)",
    "hrvatski",
    "hrvatski (Hrvatska)",
    "magyar",
    "magyar (Magyarország)",
    "Bahasa Indonesia",
    "Bahasa Indonesia (Indonesia)",
    "íslenska",
    "íslenska (Ísland)",
    "italiano",
    "italiano (Svizzera)",
    "italiano (Italia)",
    "עברית",
    "עברית (ישראל)",
    "日本語",
    "日本語 (日本)",
    "한국어",
    "한국어 (대한민국)",
    "Lietuvių",
    "Lietuvių (Lietuva)",
    "Latviešu",
    "Latviešu (Latvija)",
    "македонски",
    "македонски (Македонија)",
    "Bahasa Melayu",
    "Bahasa Melayu (Malaysia)",
    "Malti",
    "Malti (Malta)",
    "Nederlands",
    "Nederlands (België)",
    "Nederlands (Nederland)",
    "norsk",
    "norsk (Norge)",
    "norsk (Norge,nynorsk)",
    "polski",
    "polski (Polska)",
    "português",
    "português (Brasil)",
    "português (Portugal)",
    "română",
    "română (România)",
    "русский",
    "русский (Россия)",
    "Slovenčina",
    "Slovenčina (Slovenská republika)",
    "Slovenščina",
    "Slovenščina (Slovenija)",
    "shqip",
    "shqip (Shqipëria)",
    "Српски",
    "Српски (Босна и Херцеговина)",
    "Srpski (Latin,Bosna i Hercegovina)",
    "Српски (Србија и Црна Гора)",
    "Српски (Montenegro)",
    "Srpski (Latin,Crna Gora)",
    "Српски (Serbia)",
    "Srpski (Latin,Srbija)",
    "Srpski (Latin)",
    "svenska",
    "svenska (Sverige)",
    "ไทย",
    "ไทย (ประเทศไทย)",
    "Türkçe",
    "Türkçe (Türkiye)",
    "українська",
    "українська (Україна)",
    "Tiếng Việt",
    "Tiếng Việt (Việt Nam)",
    "中文",
    "中文 (中国)",
    "中文 (香港)",
    "中文 (新加坡)",
    "中文 (台灣)",
)