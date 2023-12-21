package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesThings_de : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "Mond",
        "Sonne",
        "Wasser",
        "Joseph",
        "Maria",
    )

    override val locale = forLocaleTag("de")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}