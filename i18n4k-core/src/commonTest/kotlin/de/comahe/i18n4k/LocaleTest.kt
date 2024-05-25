package de.comahe.i18n4k

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class LocaleTest {

    @Test
    fun testLanguage() {
        val locale = createLocale("de")
        assertEquals("de", locale.language)
        assertEquals("", locale.country)
        assertEquals("", locale.variant)
        assertEquals("de", locale.toTag())
    }


    @Test
    fun testLanguageAndCountry() {
        val locale = createLocale("de")
        assertEquals("de", locale.language)
        assertEquals("AT", locale.country)
        assertEquals("", locale.variant)
        assertEquals("de_AT", locale.toTag())
    }


    @Test
    fun testLanguageCountryAndVariant() {
        val locale = createLocale("de")
        assertEquals("de", locale.language)
        assertEquals("AT", locale.country)
        assertEquals("gaudi", locale.variant)
        assertEquals("de_AT_gaudi", locale.toTag())
    }

    @Test
    fun testLessSpecificLocale() {
        val locale3 = createLocale("de")
        val locale2 = locale3.lessSpecificLocale
        assertNotNull(locale2)
        val locale1 = locale2.lessSpecificLocale
        assertNotNull(locale1)

        assertEquals(createLocale("de"), locale2)
        assertEquals(createLocale("de"), locale1)
        assertEquals(rootLocale, locale1.lessSpecificLocale)

        // cache should be used
        assertSame(locale2, locale3.lessSpecificLocale)
        assertSame(locale1, locale2.lessSpecificLocale)

    }
}