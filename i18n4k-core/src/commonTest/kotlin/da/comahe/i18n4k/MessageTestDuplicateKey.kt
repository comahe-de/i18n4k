package da.comahe.i18n4k

import da.comahe.i18n4k.MessageTest1.HELLO_X1
import da.comahe.i18n4k.MessageTest1.HELLO_X1_2
import da.comahe.i18n4k.MessageTest1.HELLO_X2
import da.comahe.i18n4k.MessageTest1.HELLO_X3
import da.comahe.i18n4k.MessageTest1.HELLO_X4
import da.comahe.i18n4k.MessageTest1.HELLO_X5
import da.comahe.i18n4k.MessageTest1.HELLO_X5_2
import da.comahe.i18n4k.MessageTest1.HELLO_X5_3
import da.comahe.i18n4k.MessageTest1.NO
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
