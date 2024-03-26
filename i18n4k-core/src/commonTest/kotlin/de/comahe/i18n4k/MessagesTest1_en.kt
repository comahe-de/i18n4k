package de.comahe.i18n4k

import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesTest1_en : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf(
        "Yes",
        "No",
        "Hello {0}!",
        "Hello {0} & {0}!",
        "Hello {0} and {1}!",
        "Hello {0}, {1} and {2}!",
        "Hello {0}, {1}, {2} and {3}!",
        "Hello {0}, {1}, {2}, {3} and {4}!",
        "Hello {4}, {0}, {1}, {3} and {2}!",
        "Hello {1}, {1}, {4}, {4} and {0}!",
        "Hello {0}, {1}, {2}, {3}, {4} and {5}!",
        "Hello {0}, {1}, {2}, {3}, {4}, {5} and {6}!",
        "Hello {0}, {1}, {2}, {3}, {4}, {5}, {6} and {7}!",
        "Hello {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7} and {8}!",
        "Hello {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8} and {9}!",
        "Some text 1",
        "Some text 2",
        "{0} has forgotten {1, select, female {her} other {his} } {3, select, one {bag} other {{2} bags}}.",
        "It''s {0, number, %.02}!",
        "It''s a qute! Look: ''",
        "Hello {pa}!",
        "Hello {pa} & {pa}!",
        "Hello {pa} and {pb}!",
        "Hello {pa}, {pb} and {pc}!",
        "Hello {pa}, {pb}, {pc} and {pd}!",
        "Hello {pa}, {pb}, {pc}, {pd} and {pe}!",
        "Hello {pe}, {pa}, {pb}, {pd} and {pc}!",
        "Hello {pb}, {pb}, {pe}, {pe} and {pa}!",
        "Hello {pa}, {pb}, {pc}, {pd}, {pe} and {pf}!",
        "Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf} and {pg}!",
        "Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf}, {pg} and {ph}!",
        "Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf}, {pg}, {ph} and {pi}!",
        "Hello {pa}, {pb}, {pc}, {pd}, {pe}, {pf}, {pg}, {ph}, {pi} and {pj}!",
    )

    override val locale = Locale("en")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}