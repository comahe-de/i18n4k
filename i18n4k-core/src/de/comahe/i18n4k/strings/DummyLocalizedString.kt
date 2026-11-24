package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale

/**
 * Marker for strings with missing localisation.
 *
 * It is [LocalizedString] that always return the same
 * [value] in [toString].
 *
 * This API is not deprecated! It just uses the `@Deprecated` to highlight strings in the IDE that should be
 * replaced with a proper [LocalizedString] resource.
 */
@Deprecated("This string should be removed with a concrete localization.")
class DummyLocalizedString(private val value: Any) : LocalizedString {

    override fun toString(): String = value.toString()

    override fun toString(locale: Locale?) = value.toString()
}