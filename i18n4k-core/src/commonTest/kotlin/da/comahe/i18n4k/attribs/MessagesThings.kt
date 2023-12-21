package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.messages.MessageBundle
import kotlin.jvm.JvmField

object MessagesThings : MessageBundle() {

    @JvmField
    val MOON = getLocalizedString0("moon", 0)

    @JvmField
    val SUN = getLocalizedString0("sun", 1)

    @JvmField
    val WATER = getLocalizedString0("water", 2)

    @JvmField
    val JOSEPH = getLocalizedString0("joseph", 3)

    @JvmField
    val MARY = getLocalizedString0("mary", 4)


    init {
        registerMessageBundleEntries(
            MOON,
            SUN,
            WATER,
            JOSEPH,
            MARY,
        )
    }

}
