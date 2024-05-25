package de.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault.format
import de.comahe.i18n4k.messages.formatter.MessageParametersMap
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
        val locale = createLocale("en")

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


    /**
     * Test formats of
     * [de.comahe.i18n4k.messages.formatter.MessageNumberFormatters]
     */
    @Test
    fun format_parameterNumberTest() {
        val locale = createLocale("en")
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

        assertEquals("12.345,6789 #", format("{0,number} #", params, createLocale("de")))

    }

    /** Test invalid parameter notations inside the message strings. (like "{{0}") */
    @Test
    fun format_invalidParamNotionTest() {
        val locale = createLocale("en")

        assertEquals("invalid A", format("invalid {0,!", listOf("A"), locale))
        assertEquals("invalid 0}!", format("invalid 0}!", listOf("A"), locale))
        assertEquals("invalid ", format("invalid {{0}!", listOf("A"), locale))
        assertEquals("invalid A}!", format("invalid {0}}!", listOf("A"), locale))
        assertEquals("invalid !", format("invalid {0{0}}!", listOf("A"), locale))
        assertEquals("invalid !", format("invalid {{0}0}!", listOf("A"), locale))
    }

    /**
     * Test invalid parameter notations inside the message strings. (like
     * "{{0}")
     */
    @Test
    fun format_invalidParamValues() {
        val locale = createLocale("en")

        assertEquals("abc {9}", format("abc {9}", listOf("A"), locale))
        assertEquals("abc {~}", format("abc {~}", listOf("A"), locale))
        assertEquals("abc A", format("abc {0, foo}", listOf("A"), locale))
        assertEquals("abc {~}", format("abc {~, foo}", listOf("A"), locale))

    }

    /** Test the quoting. (like "The curly: '{'") */
    @Test
    fun format_quotingTest() {
        val locale = createLocale("en")

        assertEquals("", format("'", listOf("A"), locale))
        assertEquals("'", format("''", listOf("A"), locale))
        assertEquals("'", format("'''", listOf("A"), locale))
        assertEquals("''", format("''''", listOf("A"), locale))
        assertEquals("''", format("'''''", listOf("A"), locale))
        assertEquals("'''", format("''''''", listOf("A"), locale))
        assertEquals("'''", format("'''''''", listOf("A"), locale))
        assertEquals("a", format("a'", listOf("A"), locale))
        assertEquals("a", format("'a'", listOf("A"), locale))
        assertEquals("'a", format("''a'", listOf("A"), locale))
        assertEquals("'a", format("'''a'", listOf("A"), locale))
        assertEquals("''a", format("''''a'", listOf("A"), locale))
        assertEquals("''a", format("'''''a'", listOf("A"), locale))
        assertEquals("'''a", format("''''''a'", listOf("A"), locale))
        assertEquals("a'", format("a''", listOf("A"), locale))
        assertEquals("a'", format("'a''", listOf("A"), locale))
        assertEquals("'a'", format("''a''", listOf("A"), locale))
        assertEquals("'a'", format("'''a''", listOf("A"), locale))
        assertEquals("''a'", format("''''a''", listOf("A"), locale))
        assertEquals("''a'", format("'''''a''", listOf("A"), locale))
        assertEquals("'''a'", format("''''''a''", listOf("A"), locale))
        assertEquals("a'", format("a'''", listOf("A"), locale))
        assertEquals("a'", format("'a'''", listOf("A"), locale))
        assertEquals("'a'", format("''a'''", listOf("A"), locale))
        assertEquals("'a'", format("'''a'''", listOf("A"), locale))
        assertEquals("''a'", format("''''a'''", listOf("A"), locale))
        assertEquals("''a'", format("'''''a'''", listOf("A"), locale))
        assertEquals("'''a'", format("''''''a'''", listOf("A"), locale))
        assertEquals("a''", format("a''''", listOf("A"), locale))
        assertEquals("a''", format("'a''''", listOf("A"), locale))
        assertEquals("'a''", format("''a''''", listOf("A"), locale))
        assertEquals("'a''", format("'''a''''", listOf("A"), locale))
        assertEquals("''a''", format("''''a''''", listOf("A"), locale))
        assertEquals("''a''", format("'''''a''''", listOf("A"), locale))
        assertEquals("'''a''", format("''''''a''''", listOf("A"), locale))
        assertEquals("q A {0}", format("q {0} '{0}'", listOf("A"), locale))
        assertEquals(
            "q {'} 'a'b'  '{'}' 'a''b' ''{''}'' ''a''b'' {0}  A",
            format(
                "q '{''}' ''a''b''  '''{'''}''' '''a''''b''' ''''{''''}'''' '''''a''''b''''' {0} ' {0}",
                listOf("A"),
                locale
            )
        )
        assertEquals("q {'} 'A' '{0}' ''A''", format("q '{''}' ''{0}'' '''{0}''' ''''{0}''''", listOf("A"), locale))
        assertEquals("q {0} A", format("q '{'0'}' {0}", listOf("A"), locale))
        assertEquals("q {'0} A", format("q '{''0}' {0}", listOf("A"), locale))
        assertEquals("q 'A' '' A", format("q ''{0}'' '''' {0}", listOf("A"), locale))
        assertEquals("q A' {0} {0}", format("q {0}'' '{0} {0}", listOf("A"), locale))
    }

    @Test
    fun testParameterNames() {

        val locale = forLocaleTag("en")
        val params = MessageParametersMap("a" to "1", "b" to "2", "c" to "3", "d" to "4")

        assertEquals("1 2 3 4 !", format("{a} {b} {c} {d} !", params, locale))

        assertEquals(
            "2! !",
            format("{a, select, 1 {{b}!} 2 {{c}?} other {{d}#}} !", params, locale)
        )
        assertEquals(
            "3? !",
            format("{b, select, 1 {{b}!} 2 {{c}?} other {{d}#}} !", params, locale)
        )
        assertEquals(
            "4# !",
            format("{c, select, 1 {{b}!} 2 {{c}?} other {{d}#}} !", params, locale)
        )

    }
}