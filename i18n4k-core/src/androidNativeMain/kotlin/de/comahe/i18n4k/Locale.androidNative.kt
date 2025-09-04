package de.comahe.i18n4k

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

/** The current locale of the system or user */
@OptIn(ExperimentalForeignApi::class)
actual val systemLocale: Locale
    get() {
        return CInterop.systemLocale() ?: Locale("en")
    }