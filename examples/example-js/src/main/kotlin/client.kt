import de.comahe.i18n4k.config.I18n4kConfigImmutable
import de.comahe.i18n4k.i18n4k
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot
import x.y.MyMessages

fun main() {
    // assign the global config
    i18n4k = i18n4kConfig

    val container = document.createElement("div")


    document.body!!.appendChild(container)

    val welcome = Welcome.create {
        name = "Kotlin/JS"
        i18n4k = i18n4kConfig.configProvider as I18n4kConfigImmutable
        countLocales = MyMessages.locales.size
        init = true
    }

    println("############# rendering...234567")
    val root = createRoot(container)
    root.render(welcome)
}


