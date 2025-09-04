package de.comahe.i18n4k

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.__system_property_get
import platform.posix.getenv
import sysprop.PROP_VALUE_MAX

@OptIn(ExperimentalForeignApi::class)
internal object CInterop {

    private const val PROP_LOCALE = "persist.sys.locale"
    private const val PROP_LANGUAGE = "persist.sys.language"
    private const val PROP_COUNTRY = "persist.sys.country"

    /**
     * Acquires the system [Locale] on Android natively without JNI.
     * Does not work 100% as some vendors mess with system properties.
     */
    fun systemLocale(): Locale? {
        return systemLocaleFromProperty() ?: systemLocaleFromEnvironment()
    }

    /**
     * Checks the environment if a LANG attribute exists and returns it as [Locale].
     * Pretty uncommon on Android.
     */
    private fun systemLocaleFromEnvironment(): Locale? {
        return getenv("LANG")?.toKString()?.substringBefore('.')?.trim()?.parseLocale()
    }

    /**
     * Checks the system properties to read and parse a [Locale].
     * Pretty common on Android.
     */
    private fun systemLocaleFromProperty(): Locale? {
        systemProperty(PROP_LOCALE).parseLocale()?.let {
            return it
        }

        val lang = systemProperty(PROP_LANGUAGE)
        val country = systemProperty(PROP_COUNTRY)

        return if (!lang.isNullOrBlank()) {
            buildString {
                append(lang)
                if (!country.isNullOrBlank()) {
                    append("-$country")
                }
            }.parseLocale()
        } else {
            null
        }
    }

    private fun String?.parseLocale(): Locale? {
        return try {
            this?.ifBlank { null }?.let(::forLocaleTag)?.takeUnless {
                it.getLanguage().isBlank() && it.getCountry().isBlank()
            }
        } catch (ignored: Throwable) {
            // probably an invalid tag, like "C"
            null
        }
    }

    private fun systemProperty(key: String): String? = memScoped {
        val buffer = allocArray<ByteVar>(PROP_VALUE_MAX)
        val len = __system_property_get(key, buffer)

        if (len > 0) {
            buffer.toKString().ifBlank { null }?.trim()
        } else {
            null
        }
    }
}