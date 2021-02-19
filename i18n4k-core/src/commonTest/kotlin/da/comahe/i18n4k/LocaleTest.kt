package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.country
import de.comahe.i18n4k.language
import de.comahe.i18n4k.toTag
import de.comahe.i18n4k.variant
import kotlin.test.Test
import kotlin.test.assertEquals

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
}