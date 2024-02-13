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
        "Hallo {1}, {1}, {4}, {4} und {0}!",
        "Hallo {0}, {1}, {2}, {3}, {4} und {5}!",
        "Hallo {0}, {1}, {2}, {3}, {4}, {5} und {6}!",
        "Hallo {0}, {1}, {2}, {3}, {4}, {5}, {6} und {7}!",
        "Hallo {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7} und {8}!",
        "Hallo {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8} und {9}!",
        "Etwas Text 1",
        "Etwas Text 2",
        "{0} hat {1, select, female: ihre | seine } {3, select, one: Tasche | {2} Taschen} vergessen.",
        "Es ist {0, number, %.02}!",
        "Schau''s dir an! Ein Hochkomma: ''",
    )

    override val locale = Locale("de")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}