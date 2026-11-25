package example

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.provider.MessagesProviderViaResource
import x.y.MyMessages
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests the translation files generated as *resources*, i.e. the entries of
 * `generated.resources` in the plugin's `plugin.yaml`.
 *
 * Resource loading is JVM/Android-specific, hence this test lives in `test@jvm`.
 */
class GeneratedResourcesTest {

    @Test
    fun frenchTranslationIsOnTheClasspath() {
        val config = I18n4kConfigDefault()
        i18n4k = config

        MyMessages.registerTranslation(
            MessagesProviderViaResource(pathToResource = "/x/y/MyMessages_fr.i18n4k.txt")
        )

        config.locale = forLocaleTag("fr")
        assertEquals("Salut, Kotlin!", greeting("Kotlin"))
    }
}
