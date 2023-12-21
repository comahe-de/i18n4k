package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.messages.MessageBundle
import kotlin.jvm.JvmField

object MessagesThingsAre : MessageBundle() {

    @JvmField
    val X_IS_BEAUTIFUL = getLocalizedStringFactory1("x is beautiful", 0)
    @JvmField
    val X_HAS_GENDER = getLocalizedStringFactory1("x_has_gender", 1)


    /** using "attr-none" */
    @JvmField
    val NOT_EXISTING_ATTR = getLocalizedStringFactory1("not_existing_attr", 3)

    /** using "attr-none" */
    @JvmField
    val NOT_EXISTING_ATTR_WITH_DEFAULT =
        getLocalizedStringFactory2("not_existing_attr_with_default", 4)

    init {
        registerMessageBundleEntries(
            X_IS_BEAUTIFUL,
        )
    }

}
