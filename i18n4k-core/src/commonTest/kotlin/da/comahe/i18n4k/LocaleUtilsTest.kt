package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.country
import de.comahe.i18n4k.createLocale
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.getDisplayNameInLocale
import de.comahe.i18n4k.language
import de.comahe.i18n4k.script
import de.comahe.i18n4k.variant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class LocaleUtilsTest {


    @Test
    fun getDisplayNameInLocaleTest() {
        assertEquals(
            "xy",
            createLocale("xy").getDisplayNameInLocale()
        )
        assertEquals(
            "xy (Frac)",
            createLocale("xy", "frac").getDisplayNameInLocale()
        )
        assertEquals(
            "xy (AB,Frac)",
            createLocale("xy", "frac", "AB").getDisplayNameInLocale()
        )
        assertEquals(
            "xy (AB,cdefg,Frac)",
            createLocale("xy", "frac", "ab", "CDEFG").getDisplayNameInLocale()
        )
        assertEquals(
            "xy (AB,cdefg)",
            createLocale("xy", null, "AB", "CDEFG").getDisplayNameInLocale()
        )
        assertEquals(
            "xy (cdefg)",
            createLocale("xy", null, null, "CDEFG").getDisplayNameInLocale()
        )
        assertEquals(
            "xy (AB)",
            createLocale("xy", null, "AB").getDisplayNameInLocale()
        )

        assertEquals(
            "Deutsch (Österreich,cdefg)",
            createLocale("de", null, "AT", "CDEFG").getDisplayNameInLocale()
        )
        assertEquals(
            "Deutsch (Österreich,cdefg,Frac)",
            createLocale("de", "frac", "AT", "CDEFG").getDisplayNameInLocale()
        )
        assertEquals(
            "Deutsch (Österreich)",
            createLocale("de", null, "AT").getDisplayNameInLocale()
        )
        assertEquals(
            "Deutsch",
            createLocale("de").getDisplayNameInLocale()
        )

        assertEquals(
            "norsk (Norge,nynorsk)",
            Locale("no", "NO", "NY").getDisplayNameInLocale()
        )
        assertEquals(
            "norsk (Norge)",
            createLocale("no", null, "NO").getDisplayNameInLocale()
        )
        assertEquals(
            "norsk",
            createLocale("no").getDisplayNameInLocale()
        )

        assertEquals(
            "中文 (台灣,cdefg)",
            createLocale("zh", null, "TW", "CDEFG").getDisplayNameInLocale()
        )
        assertEquals(
            "中文 (台灣)",
            createLocale("zh", null, "TW").getDisplayNameInLocale()
        )
        assertEquals(
            "中文",
            createLocale("zh").getDisplayNameInLocale()
        )

        assertEquals(
            "Српски (Serbia)",
            createLocale("sr", null, "rs").getDisplayNameInLocale()
        )

        assertEquals(
            "Српски (Montenegro)",
            createLocale("sr", null, "me").getDisplayNameInLocale()
        )
        assertEquals(
            "Српски (Latn)",
            createLocale("sr", "latn").getDisplayNameInLocale()
        )

        assertEquals(
            "Srpski (Latin,Crna Gora)",
            createLocale("sr", "latn", "me").getDisplayNameInLocale()
        )
        assertEquals(
            "Srpski (Latin,Srbija)",
            createLocale("sr", "latn", "rs").getDisplayNameInLocale()
        )
    }

    @Test
    fun forLocaleTagTest() {

        // /############# test lang
        assertLocale(
            forLocaleTag("de"),
            "de", "", "", "", null
        )

        assertLocale(
            forLocaleTag("de-de"),
            "de", "", "DE", "", null
        )

        assertLocale(
            forLocaleTag("de-123"),
            "de", "", "123", "", null
        )

        assertLocale(
            forLocaleTag("de-1234"),
            "de", "", "", "1234", null
        )

        assertLocale(
            forLocaleTag("de-saxony"),
            "de", "", "", "saxony", null
        )
        assertLocale(
            forLocaleTag("de-1abc"),
            "de", "", "", "1abc", null
        )


        assertLocale(
            forLocaleTag("de-de-saxony"),
            "de", "", "DE", "saxony", null
        )

        assertLocale(
            forLocaleTag("de-de-saxony-a-cd"),
            "de", "", "DE", "saxony", mapOf('a' to "cd")
        )

        assertLocale(
            forLocaleTag("de-de-saxony-a-cd-efg"),
            "de", "", "DE", "saxony", mapOf('a' to "cd-efg")
        )

        assertLocale(
            forLocaleTag("de-de-saxony-a-cd-efg-h-ij"),
            "de", "", "DE", "saxony", mapOf('a' to "cd-efg", 'h' to "ij")
        )

        assertLocale(
            forLocaleTag("de-saxony-a-cd-efg-h-ij"),
            "de", "", "", "saxony", mapOf('a' to "cd-efg", 'h' to "ij")
        )

        assertLocale(
            forLocaleTag("de-de-a-cd-efg-h-ij"),
            "de", "", "DE", "", mapOf('a' to "cd-efg", 'h' to "ij")
        )

        // ############# test extlang
        // not supported by Java implementation
//        assertLocale(
//            forLocaleTag("de-abc-def"),
//            "de-abc-def", "", "", "", null
//        )
//
//        assertLocale(
//            forLocaleTag("de-abc-def-de"),
//            "de-abc-def", "", "DE", "", null
//        )
//
//        assertLocale(
//            forLocaleTag("de-abc-def-hijk"),
//            "de-abc-def", "Hijk", "", "", null
//        )

        // ############# test script
        assertLocale(
            forLocaleTag("de-frac"),
            "de", "Frac", "", "", null
        )
        assertLocale(
            forLocaleTag("de-frac-de"),
            "de", "Frac", "DE", "", null
        )
        assertLocale(
            forLocaleTag("de-frac-de-saxony"),
            "de", "Frac", "DE", "saxony", null
        )
        assertLocale(
            forLocaleTag("de-frac-de-saxony-a-cd"),
            "de", "Frac", "DE", "saxony", mapOf('a' to "cd")
        )
        assertLocale(
            forLocaleTag("de-frac-de-saxony-a-cd-efg"),
            "de", "Frac", "DE", "saxony", mapOf('a' to "cd-efg")
        )
        assertLocale(
            forLocaleTag("de-frac-de-saxony-a-cd-efg-h-ij"),
            "de", "Frac", "DE", "saxony", mapOf('a' to "cd-efg", 'h' to "ij")
        )

        // empty region
        assertLocale(
            forLocaleTag("de--a-cd"),
            "de", "", "", "", mapOf('a' to "cd")
        )
        assertLocale(
            forLocaleTag("de--saxony-a-cd"),
            "de", "", "", "saxony", mapOf('a' to "cd")
        )

    }

    @Test
    fun forLocaleTag_invalid_Test() {
        assertFails { forLocaleTag("d") }
        assertFails { forLocaleTag("de-") }
        assertFails { forLocaleTag("deutscher") }
        //extlang not supported (by Java implementation)
        assertFails { forLocaleTag("de-fix") }
        assertFails { forLocaleTag("de-12") }
        assertFails { forLocaleTag("de-varianten") }
        assertFails { forLocaleTag("de-frac-deut") }
        assertFails { forLocaleTag("de-frac-de-s") }
        assertFails { forLocaleTag("de-frac-de-saxonyyyy") }
        assertFails { forLocaleTag("de-frac-de-saxony-a") }
        assertFails { forLocaleTag("de-frac-de-saxony-a-b") }
        assertFails { forLocaleTag("de-frac-de-saxony-a-bc-d") }
        assertFails { forLocaleTag("de-frac-de-saxony-a-bc-d") }
        assertFails { forLocaleTag("de-de-saxonyyyy") }
        assertFails { forLocaleTag("de-de-1ab") }
        assertFails { forLocaleTag("de-de-abcd") }
        assertFails { forLocaleTag("de-de-saxony-a") }
        assertFails { forLocaleTag("de-de-saxony-a-b") }
        assertFails { forLocaleTag("de-de-saxony-a-bc-d") }
        assertFails { forLocaleTag("de-de-saxony-a-bc-d") }

        assertFails { forLocaleTag("12") }
    }


    private fun assertLocale(
        locale: Locale,
        language: String,
        script: String,
        country: String,
        variant: String,
        extensions: Map<Char, String>?
    ) {
        println(locale)
        assertEquals(language, locale.language)
        assertEquals(script, locale.script)
        assertEquals(country, locale.country)
        assertEquals(variant, locale.variant)
        if (extensions == null)
            assertFalse(locale.hasExtensions())
        else {
            assertEquals(locale.getExtensionKeys(), extensions.keys)
            for ((key, value) in extensions) {
                assertEquals(locale.getExtension(key), value)
            }
        }
    }
}