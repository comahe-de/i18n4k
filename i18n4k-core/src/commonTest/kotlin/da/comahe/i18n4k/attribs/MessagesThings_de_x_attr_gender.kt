package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesThings_de_x_attr_gender : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "m",
        "f",
        "n",
        "m",
        "f",
    )

    override val locale = forLocaleTag("de_x_attr_gender")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}