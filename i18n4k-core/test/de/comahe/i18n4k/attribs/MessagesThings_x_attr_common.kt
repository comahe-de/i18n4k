package de.comahe.i18n4k.attribs

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesThings_x_attr_common : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "X1",
        "X2",
        "X3",
        "X4",
        "X5",
    )

    override val locale = forLocaleTag("_x_attr_common")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}