package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesThings_de_x_attr_decl_genitiv : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "Mondes",
        null,
        null,
        null,
        null,
    )

    override val locale = forLocaleTag("de_x_attr_decl_genitiv")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}