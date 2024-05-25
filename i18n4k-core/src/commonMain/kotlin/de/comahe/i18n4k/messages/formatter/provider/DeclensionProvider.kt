package de.comahe.i18n4k.messages.formatter.provider

import de.comahe.i18n4k.strings.LocalizedAttributable
import de.comahe.i18n4k.strings.LocalizedString

interface DeclensionProvider {
    fun getDeclensionOf(declensionCase: CharSequence, value: Any?, locale: Locale?): String?
}

object DeclensionProviderDefault : DeclensionProvider {
    private const val DECLENSION_ATTRIBUTE_PREFIX = "decl-"
    override fun getDeclensionOf(
        declensionCase: CharSequence,
        value: Any?,
        locale: Locale?
    ): String? {
        return (value as? LocalizedAttributable)?.getAttribute(
            DECLENSION_ATTRIBUTE_PREFIX + declensionCase,
            locale
        )
    }
}
