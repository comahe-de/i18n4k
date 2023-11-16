package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault.format
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageTransformFormattersTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
    }

    /**
     * Test formats of
     * [de.comahe.i18n4k.messages.formatter.types.MessageTransformFormatters]
     */
    @Test
    fun test_transform() {
        val locale = Locale("en")
        val params = listOf("hello WORLD")

        assertEquals("HELLO WORLD!", format("{~, uppercase, {0} }!", params, locale))
        assertEquals("hello world!", format("{~, lowercase, {0} }!", params, locale))
        assertEquals("Hello WORLD!", format("{~, capitalize, {0} }!", params, locale))
    }
}