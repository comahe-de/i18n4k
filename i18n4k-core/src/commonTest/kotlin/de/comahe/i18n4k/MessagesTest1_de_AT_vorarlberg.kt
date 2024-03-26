package de.comahe.i18n4k

import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesTest1_de_AT_vorarlberg : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "Ja",
        "Nein",
        "Zeawas {0}!",
        "Zeawas {0} & {0}!",
        "Zeawas {0} und {1}!",
        "Zeawas {0}, {1} und {2}!",
        "Zeawas {0}, {1}, {2} und {3}!",
        "Zeawas {0}, {1}, {2}, {3} und {4}!",
        "Zeawas {4}, {0}, {1}, {3} und {2}!",
        "Zeawas {1}, {1}, {2}, {2} und {0}!",
        "Zeawas {0}, {1}, {2}, {3}, {4} und {5}!",
        "Zeawas {0}, {1}, {2}, {3}, {4}, {5} und {6}!",
        "Zeawas {0}, {1}, {2}, {3}, {4}, {5}, {6} und {7}!",
        "Zeawas {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7} und {8}!",
        "Zeawas {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8} und {9}!",
        null,
        null,
    )

    override val locale = Locale("de", "AT", "vorarlberg")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}