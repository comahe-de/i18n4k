package da.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesPlurals_pl : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "{0}. rząd",
        "{0} {0, plural, one: miesiąc | few: miesiące | many: miesięcy | miesiąca }",
    )

    override val locale = forLocaleTag("pl")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}