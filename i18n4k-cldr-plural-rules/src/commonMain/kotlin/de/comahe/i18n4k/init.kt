package de.comahe.i18n4k

import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import de.comahe.i18n4k.messages.formatter.MessagePluralCardinalFormatter
import de.comahe.i18n4k.messages.formatter.MessagePluralOrdinalFormatter

fun i18n4kInitCldrPluralRules() {
    MessageFormatterDefault.registerMessageValueFormatters(
        MessagePluralCardinalFormatter,
        MessagePluralOrdinalFormatter
    )
}