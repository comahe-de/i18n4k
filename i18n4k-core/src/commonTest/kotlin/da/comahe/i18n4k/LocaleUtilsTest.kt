package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.getDisplayNameInLocale
import kotlin.test.Test
import kotlin.test.assertEquals

class LocaleUtilsTest {

    @Test
    fun forLanguageTagTest() {
        assertEquals(
            Locale("en", "US", "WIN"),
            forLocaleTag("en_US_WIN")
        )

        assertEquals(
            Locale("en", "US", "WIN_123"),
            forLocaleTag("en_US_WIN_123")
        )

        assertEquals(
            Locale("en", "US"),
            forLocaleTag("en_US")
        )

        assertEquals(
            Locale("en", "US"),
            forLocaleTag("en_US_")
        )

        assertEquals(
            Locale("en"),
            forLocaleTag("en")
        )

        assertEquals(
            Locale("en"),
            forLocaleTag("en_")
        )
    }


    @Test
    fun getDisplayNameInLocaleTest() {
        assertEquals("xy (AB,CD)", Locale("xy", "AB", "CD").getDisplayNameInLocale())
        assertEquals("xy (AB)", Locale("xy", "AB").getDisplayNameInLocale())
        assertEquals("xy", Locale("xy").getDisplayNameInLocale())

        assertEquals("Deutsch (Österreich,XYZ)", Locale("de", "AT", "XYZ").getDisplayNameInLocale())
        assertEquals("Deutsch (Österreich)", Locale("de", "AT").getDisplayNameInLocale())
        assertEquals("Deutsch", Locale("de").getDisplayNameInLocale())

        assertEquals("norsk (Norge,nynorsk)", Locale("no", "NO", "NY").getDisplayNameInLocale())
        assertEquals("norsk (Norge)", Locale("no", "NO").getDisplayNameInLocale())
        assertEquals("norsk", Locale("no", ).getDisplayNameInLocale())

        assertEquals("中文 (台灣,foo)", Locale("zh", "TW", "foo").getDisplayNameInLocale())
        assertEquals("中文 (台灣)", Locale("zh", "TW").getDisplayNameInLocale())
        assertEquals("中文", Locale("zh").getDisplayNameInLocale())
    }
}