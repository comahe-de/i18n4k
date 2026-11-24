package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.cldr.plurals.PluralRule
import de.comahe.i18n4k.cldr.plurals.PluralRuleType

/** See [MessagePluralFormatter] for type "ordinal" */
object MessagePluralOrdinalFormatter : MessagePluralFormatter() {

    override val typeId: String
        get() = "ordinal"

    override fun getPluralRule(locale: Locale): PluralRule? {
        return PluralRule.create(locale, PluralRuleType.ORDINAL)
    }
}