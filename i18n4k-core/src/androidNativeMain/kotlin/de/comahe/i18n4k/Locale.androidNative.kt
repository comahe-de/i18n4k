package de.comahe.i18n4k

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

/** The current locale of the system or user */
@OptIn(ExperimentalForeignApi::class)
actual val systemLocale: Locale
    get() {
        val full = getenv("LANG")?.toKString()?.substringBefore('.')?.ifBlank { null }
        val lang = full?.substringBefore('_')?.takeIf { it.length in 2..3 }?.ifBlank { null }
        val country = full?.substringAfter('_')?.takeIf { it.length in 2..3 }?.ifBlank { null }

        return lang?.let {
            createLocale(
                language = it,
                country = country,
            )
        } ?: Locale("en")
    }