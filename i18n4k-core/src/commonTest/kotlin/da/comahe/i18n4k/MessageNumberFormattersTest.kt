package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault.format
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageNumberFormattersTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
    }

    /**
     * Test formats of
     * [de.comahe.i18n4k.messages.formatter.types.MessageNumberFormatters]
     */
    @Test
    fun format_parameterNumberTest() {
        val locale = Locale("en")
        val params = listOf(12345.6789)

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


    }

    @Test
    fun format_parameterNumberTest_de() {
        val locale = Locale("de")
        val params = listOf(12345.6789)

        assertEquals("12.345,6789 #", format("{0,number} #", params, locale))
    }
}