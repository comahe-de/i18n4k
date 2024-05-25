package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.cldr.plurals.PluralRule
import de.comahe.i18n4k.cldr.plurals.PluralRuleType

/** See [MessagePluralFormatter] for type "plural"/"cardinal" */
object MessagePluralCardinalFormatter : MessagePluralFormatter() {

    override val typeId: String
        get() = "plural"

    override fun getPluralRule(locale: Locale): PluralRule? {
        return PluralRule.create(locale, PluralRuleType.CARDINAL)
    }
}