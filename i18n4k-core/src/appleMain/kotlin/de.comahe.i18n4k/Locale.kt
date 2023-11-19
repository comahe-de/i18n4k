package de.comahe.i18n4k

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.variantCode

actual val systemLocale: Locale
    get() {
        val locale = NSLocale.currentLocale
        return Locale(
            language = locale.languageCode,
            country = locale.countryCode.orEmpty(),
            variant = locale.variantCode.orEmpty()
        )
    }