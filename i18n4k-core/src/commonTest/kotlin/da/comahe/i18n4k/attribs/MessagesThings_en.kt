package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesThings_en : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "moon",
        "sun",
        "water",
        "Joseph",
        "Mary",
    )

    override val locale = forLocaleTag("en")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}