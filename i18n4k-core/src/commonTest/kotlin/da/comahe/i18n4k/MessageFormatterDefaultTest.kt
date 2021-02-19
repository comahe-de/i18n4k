package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault.format
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageFormatterDefaultTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
    }

    /** Tests the setting of simple parameters. */
    @Test
    fun format_parameterTest() {
        val locale = Locale("en")

        assertEquals("A", format("{0}", listOf("A"), locale))
        assertEquals("-A-", format("-{0}-", listOf("A"), locale))
        assertEquals("{1}", format("{1}", listOf("A"), locale))
        assertEquals("A-A", format("{0}-{0}", listOf("A"), locale))


        assertEquals("A-B-C", format("{0}-{1}-{2}", listOf("A", "B", "C"), locale))
        assertEquals("C-A-B", format("{2}-{0}-{1}", listOf("A", "B", "C"), locale))
        assertEquals("A-A-A", format("{0}-{0}-{0}", listOf("A", "B", "C"), locale))
        assertEquals(
            "B-B-C-C-A-A",
            format("{1}-{1}-{2}-{2}-{0}-{0}", listOf("A", "B", "C"), locale)
        )
    }



    /** Test formats of [de.comahe.i18n4k.impl.MessageNumberFormatter] */
    @Test
    fun format_parameterNumberTest() {
        val locale = Locale("en")
        val params = listOf(12345.6789);

        assertEquals("12,345.6789 #", format("{0,number} #", params, locale))
        assertEquals("12,345.68 #", format("{0,number, %.2} #", params, locale))
        assertEquals("12,345.6789 #", format("{0,number, %.6} #", params, locale))
        assertEquals("12,345.678900 #", format("{0,number, %.06} #", params, locale))
        assertEquals("12.3456789 km #", format("{0,length} #", params, locale))
        assertEquals("12.35 km #", format("{0,length, %.2} #", params, locale))
        assertEquals("12.3456789 km #", format("{0,length, %.9} #", params, locale))
        assertEquals("12.345678900 km #", format("{0,length, %.09} #", params, locale))
        assertEquals("12,345.6789 m² #", format("{0,area} #", params, locale))
        assertEquals("12,345.68 m² #", format("{0,area, %.2} #", params, locale))
        assertEquals("12,345.6789 m² #", format("{0,area, %.6} #", params, locale))
        assertEquals("12,345.678900 m² #", format("{0,area, %.06} #", params, locale))
        assertEquals("12.3456789 kg #", format("{0,weight} #", params, locale))
        assertEquals("12.35 kg #", format("{0,weight, %.2} #", params, locale))
        assertEquals("12.3456789 kg #", format("{0,weight, %.9} #", params, locale))
        assertEquals("12.345678900 kg #", format("{0,weight, %.09} #", params, locale))
        assertEquals("3:25:46 h #", format("{0,timespan} #", params, locale))

        assertEquals("12.345,6789 #", format("{0,number} #", params, Locale("de")))

    }

    /** Test invalid parameter notations inside the message strings. (like "{{0}") */
    @Test
    fun format_invalidParamNotionTest() {
        val locale = Locale("en")

        assertEquals("invalid ", format("invalid {0!", listOf("A"), locale))
        assertEquals("invalid 0}!", format("invalid 0}!", listOf("A"), locale))
        assertEquals("invalid {{0}!", format("invalid {{0}!", listOf("A"), locale))
        assertEquals("invalid A}!", format("invalid {0}}!", listOf("A"), locale))
        assertEquals("invalid {0{0}}!", format("invalid {0{0}}!", listOf("A"), locale))
        assertEquals("invalid {{0}0}!", format("invalid {{0}0}!", listOf("A"), locale))
    }

    /** Test the quoting. (like "The curly: '{'") */
    @Test
    fun format_quotingTest() {
        val locale = Locale("en")

        assertEquals("q A {0}", format("q {0} '{0}'", listOf("A"), locale))
        assertEquals("q {0} A", format("q '{'0'}' {0}", listOf("A"), locale))
        assertEquals("q {0} A", format("q '{''0}' {0}", listOf("A"), locale))
        assertEquals("q 'A' '' A", format("q ''{0}'' '''' {0}", listOf("A"), locale))
        assertEquals("q A' {0} {0}", format("q {0}'' '{0} {0}", listOf("A"), locale))
    }
}