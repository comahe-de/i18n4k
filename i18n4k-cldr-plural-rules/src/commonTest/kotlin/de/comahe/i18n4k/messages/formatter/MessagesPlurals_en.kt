package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesPlurals_en : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "{0}{0, ordinal,  one {st} two {nd} few {rd} other {th}} rank",
        "{0} {0, plural, one {month} other {months}}",
    )

    override val locale = forLocaleTag("en")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}