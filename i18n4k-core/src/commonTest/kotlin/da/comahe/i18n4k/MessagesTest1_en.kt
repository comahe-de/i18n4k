package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
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
        "{0} has forgotten {1, select, female: her | his } {3, select, one: bag | {2} bags}.",
        "It''s {0, number, %.02}!",

    )

    override val locale = Locale("en")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}