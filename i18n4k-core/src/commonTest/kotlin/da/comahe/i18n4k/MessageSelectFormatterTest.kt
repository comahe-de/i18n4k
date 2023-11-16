package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault.format
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageSelectFormatterTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
    }


    @Test
    fun test_select_simple() {
        val locale = Locale("en")
        val pattern = "{0, select, 0:zero|1/2:few|3/4/5/6:many|too much }";

        assertEquals("zero", format(pattern, listOf(0), locale))
        assertEquals("few", format(pattern, listOf(1), locale))
        assertEquals("few", format(pattern, listOf("2"), locale))
        assertEquals("many", format(pattern, listOf(3), locale))
        assertEquals("many", format(pattern, listOf(4), locale))
        assertEquals("many", format(pattern, listOf("5"), locale))
        assertEquals("many", format(pattern, listOf("6"), locale))
        assertEquals("too much", format(pattern, listOf(7), locale))
        assertEquals("too much", format(pattern, listOf("some thing"), locale))
    }

    @Test
    fun test_select_nested() {
        val locale = Locale("en")
        val pattern = "{0, select, 0:{1}|1/2:{2}|3/4/5/6:{3}|{4} }";
        val extraParams = listOf("zero", "few", "many", "too much")

        assertEquals("zero", format(pattern, listOf(0) + extraParams, locale))
        assertEquals("few", format(pattern, listOf(1) + extraParams, locale))
        assertEquals("few", format(pattern, listOf("2") + extraParams, locale))
        assertEquals("many", format(pattern, listOf(3) + extraParams, locale))
        assertEquals("many", format(pattern, listOf(4) + extraParams, locale))
        assertEquals("many", format(pattern, listOf("5") + extraParams, locale))
        assertEquals("many", format(pattern, listOf("6") + extraParams, locale))
        assertEquals("too much", format(pattern, listOf(7) + extraParams, locale))
        assertEquals("too much", format(pattern, listOf("some thing") + extraParams, locale))
    }

    @Test
    fun test_select_regex() {
        val locale = Locale("en")
        val pattern =
            "{0, select, 0:zero | regex#\\d+ : digits | regex#\\w+ : word | regex#[abc<>-]+ : mix | else }"

        assertEquals("zero", format(pattern, listOf(0), locale))
        assertEquals("digits", format(pattern, listOf(1), locale))
        assertEquals("digits", format(pattern, listOf(12), locale))
        assertEquals("digits", format(pattern, listOf("001200"), locale))
        assertEquals("word", format(pattern, listOf('a'), locale))
        assertEquals("word", format(pattern, listOf("b"), locale))
        assertEquals("word", format(pattern, listOf("abc"), locale))
        assertEquals("word", format(pattern, listOf("abc".subSequence(0, 1)), locale))
        assertEquals("word", format(pattern, listOf("a1"), locale))
        assertEquals("word", format(pattern, listOf("b2"), locale))
        assertEquals("word", format(pattern, listOf("1a"), locale))
        assertEquals("word", format(pattern, listOf("2b"), locale))
        assertEquals("mix", format(pattern, listOf("<a>"), locale))
        assertEquals("mix", format(pattern, listOf(">"), locale))
        assertEquals("mix", format(pattern, listOf("<"), locale))
        assertEquals("mix", format(pattern, listOf("----"), locale))
        assertEquals("else", format(pattern, listOf("#"), locale))
        assertEquals("else", format(pattern, listOf(";;;"), locale))
    }

    @Test
    fun test_invalid() {
        val locale = Locale("en")

        assertEquals("!", format("{~, select }!", listOf('a'), locale))
        assertEquals("!", format("{~, select , }!", listOf('a'), locale))
        assertEquals("!", format("{~, select , a: x }!", listOf('a'), locale))

        assertEquals("!", format("{0, select }!", listOf('a'), locale))
        assertEquals("!", format("{0, select , }!", listOf('a'), locale))
        assertEquals("!", format("{0, select , b: y }!", listOf('a'), locale))
        assertEquals("!", format("{0, select ,  |  x  }!", listOf('a'), locale))
        assertEquals("!", format("{0, select ,  | a: x  }!", listOf('a'), locale))

        assertEquals("{1}!", format("{1, select ,  x  }!", listOf('a'), locale))
        assertEquals("{1}!", format("{1, select ,  a: x  }!", listOf('a'), locale))
    }
}