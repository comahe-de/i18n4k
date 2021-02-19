package de.comahe.i18n4k


actual typealias Locale = java.util.Locale

actual val systemLocale: Locale
    get() {
        val userLanguage = System.getProperty("user.language")
            ?: return Locale.getDefault()
        return java.util.Locale(userLanguage)
    }