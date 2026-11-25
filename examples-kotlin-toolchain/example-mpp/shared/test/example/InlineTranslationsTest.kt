package example

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.i18n4k
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests the translations that are generated *into* the Kotlin code
 * (`sourceCodeLocales` in `module.yaml`). They do not depend on platform
 * resource loading and therefore run on every platform the module is built for.
 */
class InlineTranslationsTest {

    private lateinit var config: I18n4kConfigDefault

    @BeforeTest
    fun setUp() {
        config = I18n4kConfigDefault()
        i18n4k = config
    }

    @Test
    fun english() {
        config.locale = Locale("en")
        assertEquals("Hello, Kotlin!", greeting("Kotlin"))
    }

    @Test
    fun americanEnglish() {
        config.locale = forLocaleTag("en_US")
        assertEquals("Hi, Kotlin!", greeting("Kotlin"))
    }

    @Test
    fun german() {
        config.locale = Locale("de")
        assertEquals("Hallo, Kotlin!", greeting("Kotlin"))
    }
}
