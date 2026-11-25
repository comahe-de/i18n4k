package example.compose

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.i18n4kInitCldrPluralRules
import x.y.Actions
import x.y.MyMessages
import x.y.Things
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests the generated message bundles. All locales are inlined into the Kotlin
 * code (see `sourceCodeLocales` in `shared/module.yaml`), so these tests do not
 * depend on resource loading and run on every platform.
 */
class TranslationsTest {

    private lateinit var config: I18n4kConfigDefault

    @BeforeTest
    fun setUp() {
        config = I18n4kConfigDefault()
        i18n4k = config
        i18n4kInitCldrPluralRules()
    }

    @Test
    fun allLocalesAreInlined() {
        assertEquals(6, MyMessages.locales.size)
    }

    @Test
    fun greetingInAllLocales() {
        config.locale = forLocaleTag("en")
        assertEquals("Hello, Kotlin!", MyMessages.sayHello("Kotlin"))

        config.locale = forLocaleTag("en_US")
        assertEquals("Hi, Kotlin!", MyMessages.sayHello("Kotlin"))

        config.locale = forLocaleTag("de")
        assertEquals("Hallo, Kotlin!", MyMessages.sayHello("Kotlin"))

        config.locale = forLocaleTag("fr")
        assertEquals("Salut, Kotlin!", MyMessages.sayHello("Kotlin"))

        config.locale = forLocaleTag("nl")
        assertEquals("Hallo, Kotlin!", MyMessages.sayHello("Kotlin"))

        config.locale = forLocaleTag("en_US_texas")
        assertEquals("Howdy, Kotlin!", MyMessages.sayHello("Kotlin"))
    }

    @Test
    fun pluralAndOrdinal() {
        config.locale = forLocaleTag("en")

        assertEquals("The sun has 1 form.", Actions.shape_count(1, Things.sun))
        assertEquals("The sun has 2 forms.", Actions.shape_count(2, Things.sun))
        assertEquals(
            "The 1st shape of the sun is round.",
            Actions.shape_order(1, Things.sun),
        )
        assertEquals(
            "The 2nd shape of the sun is round.",
            Actions.shape_order(2, Things.sun),
        )
    }

    @Test
    fun attributes() {
        config.locale = forLocaleTag("en")

        assertEquals("m", Things.boy.getAttribute("gender"))
        assertEquals("☀️", Things.sun.getAttribute("emoji"))
        assertEquals(
            "Sun - The attribute \"gender\" is neuter.",
            Actions.attr_select(Things.sun),
        )
    }

    @Test
    fun germanTranslationUsesInlineLocale() {
        config.locale = forLocaleTag("de")

        assertEquals("Gegenstand auswählen:", Actions.select_thing())
        assertEquals("Sonne", Things.sun.toString())
        assertEquals("f", Things.sun.getAttribute("gender"))
    }
}
