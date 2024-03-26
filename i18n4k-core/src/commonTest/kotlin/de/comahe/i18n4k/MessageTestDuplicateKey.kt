package de.comahe.i18n4k

import de.comahe.i18n4k.messages.MessageBundle
import kotlin.jvm.JvmField

object MessageTestDuplicateKey : MessageBundle() {

    /* yes */
    @JvmField
    val YES = getLocalizedString0("YES", 0)

    /* no */
    @JvmField
    val YES2 = getLocalizedString0("YES", 1)


    init {
        registerMessageBundleEntries(YES, YES2)
    }

}
