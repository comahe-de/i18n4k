package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.providers.MessagesProvider
import kotlin.jvm.JvmStatic

object MessagesTest1_xy : MessagesProvider {

    @JvmStatic
    private val data: Array<String?> = arrayOf("")

    override val locale = Locale("xy")

    override val size = data.size

    override fun get(index: Int): String? = data[index]

}