package de.comahe.i18n4k

import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesTest1_de_AT : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "Ja",
        "Nein",
        "Servus {0}!",
        "Servus {0} & {0}!",
        "Servus {0} und {1}!",
        "Servus {0}, {1} und {2}!",
        "Servus {0}, {1}, {2} und {3}!",
        "Servus {0}, {1}, {2}, {3} und {4}!",
        "Servus {4}, {0}, {1}, {3} und {2}!",
        "Servus {1}, {1}, {2}, {2} und {0}!",
        "Servus {0}, {1}, {2}, {3}, {4} und {5}!",
        "Servus {0}, {1}, {2}, {3}, {4}, {5} und {6}!",
        "Servus {0}, {1}, {2}, {3}, {4}, {5}, {6} und {7}!",
        "Servus {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7} und {8}!",
        "Servus {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8} und {9}!",
        "Etwas Text 1 für AT",
        null,
    )

    override val locale = Locale("de", "AT")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}