package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesThingsAre_en : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "The {0} is beautiful. You will love {0, attr:gender, m:him | f:her | n:it }!",
        "The {0} is beautiful. You will love {0, gender, m:him | f:her | n:it }!",
        "{0} has gender {0, attr-gender}",
        "Not existing: {0, attr-none}",
        "Not existing with default: {0, attr-none, {1}! }",
    )

    override val locale = forLocaleTag("en")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}