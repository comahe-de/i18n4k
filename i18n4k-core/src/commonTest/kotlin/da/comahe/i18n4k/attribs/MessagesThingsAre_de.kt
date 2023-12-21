package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesThingsAre_de : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "{0,  attr:gender, m:Der | f:Die | n:Das } {0} ist schön. Du wirst {0, attr:gender, m:ihn | f:sie  | n:es} lieben!",
        "{0} hat Geschlecht {0, attr-gender}",
        "Nicht existent: {0, attr-none}",
        "Nicht existent mit Default: {0, attr-none, {1}! }",
    )

    override val locale = forLocaleTag("de")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}