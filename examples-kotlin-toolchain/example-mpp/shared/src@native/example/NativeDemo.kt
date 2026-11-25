package example

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.i18n4k

/**
 * Console demo shared by all Kotlin/Native app modules
 * (`windows-app`, `linux-app`, `macos-app`).
 *
 * Only the inline translations are used here: loading the generated resource
 * files on Kotlin/Native is platform-specific and not part of this example
 * (the Gradle example has the same limitation).
 */
fun runNativeDemo() {
    val config = I18n4kConfigDefault()
    i18n4k = config

    for (tag in listOf("en", "en_US", "de")) {
        config.locale = forLocaleTag(tag)
        println("[$tag] ${title()}")
        println("[$tag] ${greeting("Kotlin/Native")}")
    }
}
