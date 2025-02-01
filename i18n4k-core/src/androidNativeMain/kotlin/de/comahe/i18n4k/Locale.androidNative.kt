package de.comahe.i18n4k

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

/** The current locale of the system or user */
@OptIn(ExperimentalForeignApi::class)
actual val systemLocale: Locale
    get() {
        var locale: Locale? = null
        try {
            val tag = getenv("LANG")?.toKString()?.substringBefore('.')?.trim()?.ifBlank { null }

            locale = tag?.let(::forLocaleTag)?.takeUnless {
                it.getLanguage().isBlank() && it.getCountry().isBlank()
            }
        } catch (ignore: Throwable) {
            //probably an invalid tag, like "C"
        }
        if (locale == null) {
            locale = Locale("en")
        }
        return locale
    }