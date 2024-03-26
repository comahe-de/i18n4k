package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.messages.MessageBundle
import kotlin.jvm.JvmField

object MessagesPlurals : MessageBundle() {

    @JvmField
    val RANK_ORDINAL = getLocalizedStringFactory1("rand", 0)

    @JvmField
    val MONTH_COUNT = getLocalizedStringFactory1("month", 1)

    init {
        registerMessageBundleEntries(
            RANK_ORDINAL,
            MONTH_COUNT,
        )
    }

}
