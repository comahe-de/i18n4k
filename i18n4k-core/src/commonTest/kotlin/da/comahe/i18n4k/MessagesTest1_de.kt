package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesTest1_de : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "Ja",
        "Nein",
        "Hallo {0}!",
        "Hallo {0} & {0}!",
        "Hallo {0} und {1}!",
        "Hallo {0}, {1} und {2}!",
        "Hallo {0}, {1}, {2} und {3}!",
        "Hallo {0}, {1}, {2}, {3} und {4}!",
        "Hallo {4}, {0}, {1}, {3} und {2}!",
        "Hallo {1}, {1}, {2}, {2} und {0}!",
    )

    override val locale = Locale("de")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}