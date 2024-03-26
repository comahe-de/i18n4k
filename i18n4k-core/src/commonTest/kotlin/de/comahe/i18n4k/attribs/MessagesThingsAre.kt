package de.comahe.i18n4k.attribs

import de.comahe.i18n4k.messages.MessageBundle
import kotlin.jvm.JvmField

object MessagesThingsAre : MessageBundle() {

    /** using "attr:gender" */
    @JvmField
    val X_IS_BEAUTIFUL = getLocalizedStringFactory1("x is beautiful", 0)

    /** using "gender" */
    @JvmField
    val X_IS_BEAUTIFUL_2 = getLocalizedStringFactory1("x is beautiful 2", 1)

    /** using "attr-gender" */
    @JvmField
    val X_HAS_GENDER = getLocalizedStringFactory1("x_has_gender", 2)

    /** using "attr-none" */
    @JvmField
    val NOT_EXISTING_ATTR = getLocalizedStringFactory1("not_existing_attr", 3)

    /** using "attr-none2" */
    @JvmField
    val NOT_EXISTING_ATTR2 = getLocalizedStringFactory1("not_existing_attr2", 4)

    /** using "attr-none" */
    @JvmField
    val NOT_EXISTING_ATTR_WITH_DEFAULT =
        getLocalizedStringFactory2("not_existing_attr_with_default", 5)

    /** using "gender" and "declension" */
    @JvmField
    val THE_COLOR_OF_X = getLocalizedStringFactory1("the color of x", 6)


    init {
        registerMessageBundleEntries(
            X_IS_BEAUTIFUL, X_IS_BEAUTIFUL_2, X_HAS_GENDER, NOT_EXISTING_ATTR, NOT_EXISTING_ATTR2,
            NOT_EXISTING_ATTR_WITH_DEFAULT, THE_COLOR_OF_X
        )
    }

}
