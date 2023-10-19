package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.country
import de.comahe.i18n4k.language
import de.comahe.i18n4k.lessSpecificLocale
import de.comahe.i18n4k.toTag
import de.comahe.i18n4k.variant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class LocaleTest {

    @Test
    fun testLanguage() {
        val locale = Locale("de")
        assertEquals("de", locale.language)
        assertEquals("", locale.country)
        assertEquals("", locale.variant)
        assertEquals("de", locale.toTag())
    }


    @Test
    fun testLanguageAndCountry() {
        val locale = Locale("de", "AT")
        assertEquals("de", locale.language)
        assertEquals("AT", locale.country)
        assertEquals("", locale.variant)
        assertEquals("de_AT", locale.toTag())
    }


    @Test
    fun testLanguageCountryAndVariant() {
        val locale = Locale("de", "AT", "gaudi")
        assertEquals("de", locale.language)
        assertEquals("AT", locale.country)
        assertEquals("gaudi", locale.variant)
        assertEquals("de_AT_gaudi", locale.toTag())
    }

    @Test
    fun testLessSpecificLocale() {
        val locale3 = Locale("de", "AT", "gaudi")
        val locale2 = locale3.lessSpecificLocale
        assertNotNull(locale2)
        val locale1 = locale2.lessSpecificLocale
        assertNotNull(locale1)

        assertEquals(Locale("de", "AT"), locale2)
        assertEquals(Locale("de"), locale1)
        assertNull(locale1.lessSpecificLocale)

        // cache should be used
        assertSame(locale2, locale3.lessSpecificLocale)
        assertSame(locale1, locale2.lessSpecificLocale)

    }
}