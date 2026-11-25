package example.js

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.i18n4k
import example.greeting
import example.title
import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

/**
 * Plain-DOM browser app. Only the inline translations are used: the example
 * does not load the generated resource files via HTTP (the Gradle example uses
 * React instead; here the focus is on the i18n4k plugin).
 */
fun main() {
    val config = I18n4kConfigDefault()
    i18n4k = config

    val root = document.createElement("div") as HTMLDivElement
    document.body!!.appendChild(root)

    val titleElement = document.createElement("h1") as HTMLElement
    val input = document.createElement("input") as HTMLInputElement
    input.value = "Kotlin"
    val messageElement = document.createElement("p") as HTMLElement
    val buttonsElement = document.createElement("div") as HTMLDivElement

    fun update() {
        titleElement.textContent = title()
        messageElement.textContent = greeting(input.value)
    }

    for (tag in listOf("en", "en_US", "de")) {
        val locale = forLocaleTag(tag)
        val button = document.createElement("button") as HTMLButtonElement
        button.textContent = locale.getDisplayNameInLocale()
        button.addEventListener("click", {
            config.locale = locale
            update()
        })
        buttonsElement.appendChild(button)
    }
    input.addEventListener("input", { update() })

    root.appendChild(titleElement)
    root.appendChild(input)
    root.appendChild(messageElement)
    root.appendChild(buttonsElement)

    update()
}
