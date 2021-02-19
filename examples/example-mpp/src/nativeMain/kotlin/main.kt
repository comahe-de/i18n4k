import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import x.y.MyMessages

fun main() {
    val i18n4kConfig = I18n4kConfigDefault()
    i18n4k = i18n4kConfig

    // load "fr" and "nl" at runtime
    // TODO: how to load resources from the "src/main/resources" folder? Can it be packed into the executable and loaded?

    while (true) {
        println("locale = ${i18n4kConfig.locale} - ${i18n4kConfig.locale.getDisplayNameInLocale()}")
        println(MyMessages.title)
        println(MyMessages.sayHello("Kotlin/Native"))
        println(MyMessages.locales)
        val line = readLine()
        if (line == null || line == "exit")
            return
        i18n4kConfig.locale = Locale(line)
        println()
        println()
    }
}