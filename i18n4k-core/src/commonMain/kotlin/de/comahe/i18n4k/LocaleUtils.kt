@file:Suppress("unused")

package de.comahe.i18n4k

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.collections.immutable.persistentMapOf


/**
 * Property to access [Locale.getLanguage]
 *
 * Direct expected property is not possible because `java.util.Locale` should
 * be an actual typealias and getter cannot replace property currently:
 * [KT-15620](https://youtrack.jetbrains.com/issue/KT-15620)
 */
val Locale.language: String
    get() = this.getLanguage()

/** Property to access [Locale.getScript] */
val Locale.script: String
    get() = this.getScript()

/** Property to access [Locale.getCountry] */
val Locale.country: String
    get() = this.getCountry()

/** Property to access [Locale.getVariant] */
val Locale.variant: String
    get() = this.getVariant()


/**
 * Useful constant for the root locale.
 *
 * The root locale is the locale whose language, country, and variant are empty ("") strings.
 *
 * This is regarded as the base locale of all locales, and is used as the language/country neutral
 * locale for the locale sensitive operations.
 */
val rootLocale = createLocale("")


/** Cache for [lessSpecificLocale] to prevent memory allocations. */
private val lessSpecificLocaleCache = atomic(persistentMapOf<Locale, Locale?>())

/**
 * Creates a locale that is one step less specific than this locale.
 *
 * Returns [rootLocale] if there is no less specific locale.
 *
 * E.g. `de_DE_saxony` would become `de_DE`, `de_DE` would become `de`, `de` would become ``
 * ([rootLocale]), and `` ([rootLocale]) would stay as it is.
 */
val Locale.lessSpecificLocale: Locale
    get() {
        if (variant.isEmpty() && country.isEmpty())
            return rootLocale

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

/**
 * Applies all possible locales in the chain, until `block` returns a non-null value and returns
 * this value.
 *
 * Returns null if there are no more possible locales.
 *
 * This function can be used in cases when different locales should be tried to compute the chain of
 * locales that should be tried.
 *
 * @param localeToStart Start locale to check. If null [de.comahe.i18n4k.config.I18n4kConfig.locale]
 *     in [i18n4k] is used.
 */
inline fun <R> applyLocales(localeToStart: Locale? = null, block: (Locale) -> R?): R? {
    var tryDefault = true
    var localeToUse = localeToStart ?: i18n4k.locale
    while (true) {
        val result = block(localeToUse)
        if (result != null)
            return result
        if (localeToUse != rootLocale) {
            localeToUse = localeToUse.lessSpecificLocale
        } else if (tryDefault && localeToUse != i18n4k.defaultLocale) {
            tryDefault = false
            localeToUse = i18n4k.defaultLocale
        } else
            return null
    }
}

/**
 * Transforms a languageTag like "en_US_texas" to a Locale("en","US","texas")
 *
 * See [rfc5646](https://www.rfc-editor.org/rfc/rfc5646.html#section-2.1)
 */
fun forLocaleTag(languageTag: String, separator: Char = '_', separator2: Char = '-'): Locale {

    if (languageTag.isBlank())
        return rootLocale

    var language = ""
    var script = ""
    var country = ""
    var variant = ""
    val extensions = mutableMapOf<Char, String>()

    val parts = languageTag.trim().split(separator, separator2)

    // tool functions....
    fun String.isAlpha() = all { it.isLetter() }
    fun String.isDigit() = all { it.isDigit() }
    fun String.isAlphaNum() = all { it.isLetterOrDigit() }


    var index = 0;

    if (parts.size > index
        && parts[index].isAlpha()
        && parts[index].length in 2..8
    )
        language = parts[index++]

    // up to 3 extlang -> not supported by Java implementation
    //while (parts.size > index && parts[index].length == 3) {
    //    language += "-" + parts[index++];
    //}

    // skip empty parts
    while (parts.size > index && parts[index].isEmpty())
        index++


    // script
    if (parts.size > index
        && parts[index].isAlpha()
        && parts[index].length == 4
    )
        script = parts[index++]

    // skip empty parts
    while (parts.size > index && parts[index].isEmpty())
        index++


    // country / region
    if (parts.size > index
        && (
            (parts[index].length == 2 && parts[index].isAlpha())
                || (parts[index].length == 3 && parts[index].isDigit())
            )
    )
        country = parts[index++]

    // skip empty parts
    while (parts.size > index && parts[index].isEmpty())
        index++

    // variant
    if (parts.size > index
        && ((parts[index].length in 5..8 && parts[index].isAlphaNum())
            || (parts[index].length == 4 && parts[index][0].isDigit() && parts[index].isAlphaNum())
            )
    )
        variant = parts[index++]

    // skip empty parts
    while (parts.size > index && parts[index].isEmpty())
        index++


    // read extensions
    while (parts.size > index && parts[index].length == 1) {
        val key = parts[index++]
        val value = StringBuilder()
        while (parts.size > index && parts[index].length in 2..8) {
            if (value.isNotEmpty())
                value.append("-")
            value.append(parts[index++])
        }
        check(value.isNotEmpty()) { "Value for extension key '$key' is empty! language-tag: $languageTag" }
        extensions[key[0]] = value.toString()
    }
    check(parts.size <= index) { "Unexpected part: ${parts[index]} -  language-tag: $languageTag" }

    return createLocale(language, script, country, variant, extensions)
}

fun toLocaleTag(
    language: String,
    script: String,
    country: String,
    variant: String,
    extensions: Map<Char, String>?,
    separator: String = "_"
): String {
    val tag = StringBuilder()
    tag.append(language)
    if (script.isNotEmpty())
        tag.append(separator).append(script)
    if (country.isNotEmpty())
        tag.append(separator).append(country)
    if (variant.isNotEmpty())
        tag.append(separator).append(variant)
    extensions?.forEach { (key, value) ->
        tag.append(separator).append(key).append(separator).append(value)
    }
    return tag.toString()
}

fun Locale.toTag(separator: String = "_"): String {

    val extensions: Map<Char, String>?
    if (hasExtensions()) {
        extensions = mutableMapOf()
        for (key in getExtensionKeys()) {
            val value = getExtension(key)
            if (value != null)
                extensions[key] = value
        }
    } else
        extensions = null

    return toLocaleTag(language, script, country, variant, extensions, separator)
}


/**
 * Returns a name for the locale that is appropriate for display to the user in the language of the
 * locale
 */
fun Locale.getDisplayNameInLocale(): String {
    val language = this.language.lowercase()
    val script = this.script.lowercase()
    val country = this.country.lowercase()
    val variant = this.variant.lowercase()

    var display = localeTags.binarySearch(language).let {
        if (it < 0)
            this.language
        else
            localeDisplayName[it]
    }

    fun String.appendInfo(info: String): String {
        val index = indexOf(")")
        if (index < 0)
            return "$this ($info)"
        return substring(0, index) + "," + info + ")"
    }


    display =
        if (country.isEmpty())
            display
        else
            localeTags.binarySearch(language + "_" + country).let {
                if (it < 0)
                    display.appendInfo(this.country)
                else
                    localeDisplayName[it]
            }


    display =
        if (variant.isEmpty())
            display
        else
            localeTags.binarySearch(language + "_" + country + "_" + variant).let {
                if (it < 0)
                    display.appendInfo(this.variant)
                else
                    localeDisplayName[it]

            }

    display =
        if (script.isEmpty())
            display
        else
            localeTags.binarySearch(language + "_" + country + "_" + variant + "#" + script).let {
                if (it < 0)
                    display.appendInfo(this.script)
                else
                    localeDisplayName[it]

            }

    return display
}

/** Locale */
private val localeTags = arrayListOf(
    "ar",
    "ar_ae",
    "ar_bh",
    "ar_dz",
    "ar_eg",
    "ar_iq",
    "ar_jo",
    "ar_kw",
    "ar_lb",
    "ar_ly",
    "ar_ma",
    "ar_om",
    "ar_qa",
    "ar_sa",
    "ar_sd",
    "ar_sy",
    "ar_tn",
    "ar_ye",
    "be",
    "be_by",
    "bg",
    "bg_bg",
    "ca",
    "ca_es",
    "cs",
    "cs_cz",
    "da",
    "da_dk",
    "de",
    "de_at",
    "de_ch",
    "de_de",
    "de_gr",
    "de_lu",
    "el",
    "el_cy",
    "el_gr",
    "en",
    "en_au",
    "en_ca",
    "en_gb",
    "en_ie",
    "en_in",
    "en_mt",
    "en_nz",
    "en_ph",
    "en_sg",
    "en_us",
    "en_za",
    "es",
    "es_ar",
    "es_bo",
    "es_cl",
    "es_co",
    "es_cr",
    "es_cu",
    "es_do",
    "es_ec",
    "es_es",
    "es_gt",
    "es_hn",
    "es_mx",
    "es_ni",
    "es_pa",
    "es_pe",
    "es_pr",
    "es_py",
    "es_sv",
    "es_us",
    "es_uy",
    "es_ve",
    "et",
    "et_ee",
    "fi",
    "fi_fi",
    "fr",
    "fr_be",
    "fr_ca",
    "fr_ch",
    "fr_fr",
    "fr_lu",
    "ga",
    "ga_ie",
    "hi",
    "hi_in",
    "hr",
    "hr_hr",
    "hu",
    "hu_hu",
    "in",
    "in_id",
    "is",
    "is_is",
    "it",
    "it_ch",
    "it_it",
    "iw",
    "iw_il",
    "ja",
    "ja_jp",
    "ko",
    "ko_kr",
    "lt",
    "lt_lt",
    "lv",
    "lv_lv",
    "mk",
    "mk_mk",
    "ms",
    "ms_my",
    "mt",
    "mt_mt",
    "nl",
    "nl_be",
    "nl_nl",
    "no",
    "no_no",
    "no_no_ny",
    "pl",
    "pl_pl",
    "pt",
    "pt_br",
    "pt_pt",
    "ro",
    "ro_ro",
    "ru",
    "ru_ru",
    "sk",
    "sk_sk",
    "sl",
    "sl_si",
    "sq",
    "sq_al",
    "sr",
    "sr_ba",
    "sr_ba_#latn",
    "sr_cs",
    "sr_me",
    "sr_me_#latn",
    "sr_rs",
    "sr_rs_#latn",
    "sr__#latn",
    "sv",
    "sv_se",
    "th",
    "th_th",
    "tr",
    "tr_tr",
    "uk",
    "uk_ua",
    "vi",
    "vi_vn",
    "zh",
    "zh_cn",
    "zh_hk",
    "zh_sg",
    "zh_tw",
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