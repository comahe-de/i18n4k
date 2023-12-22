package da.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesPlurals_de : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "{0}. Rang",
        "{0} {0, plural, one: Monat | Monate}",
    )

    override val locale = forLocaleTag("de")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}