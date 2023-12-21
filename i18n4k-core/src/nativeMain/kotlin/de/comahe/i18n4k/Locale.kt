package de.comahe.i18n4k

import kotlinx.collections.immutable.ImmutableMap

actual typealias Locale = DefaultLocaleImpl

actual fun createLocale(
    language: String,
    script: String?,
    country: String?,
    variant: String?,
    extensions: Map<Char, String>?
) = createDefaultLocaleImpl(language, script, country, variant, extensions)