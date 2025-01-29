package de.comahe.i18n4k

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

/** The current locale of the system or user */
@OptIn(ExperimentalForeignApi::class)
actual val systemLocale: Locale
    get() {
        val tag = getenv("LANG")?.toKString()?.substringBefore('.')?.trim()?.ifBlank { null }

        return tag?.let(::forLocaleTag)?.takeUnless {
            it.getLanguage().isBlank() && it.getCountry().isBlank()
        } ?: Locale("en")
    }