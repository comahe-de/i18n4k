package de.comahe.i18n4k

import de.comahe.i18n4k.strings.capitalize
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

/** Default implementation for [Locale] for non JVM targets */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class DefaultLocaleImpl(
    private val language: String,
    private val script: String,
    private val country: String,
    private val variant: String,
    private val extensions: ImmutableMap<Char, String>,
) {
    constructor(
        language: String
    ) : this(language, "", "")

    constructor(
        language: String,
        country: String,
    ) : this(language, country, "")


    constructor(
        language: String,
        country: String,
        variant: String,
    ) : this(language, "", country, variant, persistentMapOf())

    fun getLanguage(): String = language
    fun getScript(): String = script
    fun getCountry(): String = country
    fun getVariant(): String = variant

    fun getExtension(key: Char): String? = extensions[key]

    fun getExtensionKeys(): Set<Char> = extensions.keys

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DefaultLocaleImpl) return false

        if (language != other.language) return false
        if (script != other.script) return false
        if (country != other.country) return false
        if (variant != other.variant) return false
        if (extensions != other.extensions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + script.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + variant.hashCode()
        result = 31 * result + extensions.hashCode()
        return result
    }

    override fun toString(): String {
        return toLocaleTag(language, script, country, variant, extensions)
    }
}

/** Default implementation for [createLocale] for non JVM targets */
fun createDefaultLocaleImpl(
    language: String,
    script: String?,
    country: String?,
    variant: String?,
    extensions: Map<Char, String>?
): DefaultLocaleImpl {
    val extensionsBuilder = persistentMapOf<Char, String>().builder()
    extensions?.forEach { (key, value) ->
        extensionsBuilder[key.lowercaseChar()] = value.lowercase()
    }

    return DefaultLocaleImpl(
        language.trim().lowercase(),
        script?.trim()?.capitalize() ?: "",
        country?.trim()?.uppercase() ?: "",
        variant?.trim()?.lowercase() ?: "",
        extensionsBuilder.build()
    )
}