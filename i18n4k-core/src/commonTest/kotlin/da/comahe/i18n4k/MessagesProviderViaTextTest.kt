package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.providers.MessagesProviderViaText
import de.comahe.i18n4k.config.I18n4kConfigDefault
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class MessagesProviderViaTextTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()

        MessageTest1.unregisterAllTranslations()
    }

    @Test
    fun checkLocaleTest() {
        val text =
            """
                sn
                ^
                nu
            """.trimIndent()

        var provider: MessagesProviderViaText

        // check "sn" ok
        provider = MessagesProviderViaText(Locale("sn"), text)
        assertEquals(Locale("sn"), provider.locale)


        // no check
        provider = MessagesProviderViaText(null, text)
        assertEquals(Locale("sn"), provider.locale)

        // check "en" failed
        try {
            provider = MessagesProviderViaText(Locale("en"), text)
            provider.locale // trigger loading...
            fail("Should throw that locale is not 'de'")
        } catch (ignored: IllegalArgumentException) {

        }
    }


    @Test
    fun loadTextTest() {
        val text =
            """
                sn
                ^
                nu
                ^
                ne
                ^
                ^
                
                ^
                   
                ^
                multi
                line ^
                text
                ^
                ^^ a ^
                ^
                end
            """.trimIndent()
        val provider = MessagesProviderViaText(Locale("sn"), text)

        assertEquals(Locale("sn"), provider.locale)

        assertEquals("nu", provider[0])
        assertEquals("ne", provider[1])
        assertEquals(null, provider[2])
        assertEquals("", provider[3])
        assertEquals("   ", provider[4])
        assertEquals("multi\nline ^\ntext", provider[5])
        assertEquals("^ a ^", provider[6])
        assertEquals("end", provider[7])

        i18n4kConfig.locale = Locale("sn")
        MessageTest1.registerTranslation(MessagesTest1_en)
        MessageTest1.registerTranslation(provider)

        assertEquals("nu", MessageTest1.YES())
        assertEquals("ne", MessageTest1.NO())
    }
}