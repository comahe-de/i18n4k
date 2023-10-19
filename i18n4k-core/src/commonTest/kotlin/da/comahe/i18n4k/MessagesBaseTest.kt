package da.comahe.i18n4k

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.config.I18n4kConfigDefault
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.fail

class MessageBundleTest {
    private  var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
        i18n4kConfig.locale = Locale("en")

        MessageTest1.unregisterAllTranslations()
        MessageTest1.registerTranslation(MessagesTest1_en)
        MessageTest1.registerTranslation(MessagesTest1_de)
        MessageTest1.registerTranslation(MessagesTest1_de_AT)
        MessageTest1.registerTranslation(MessagesTest1_de_AT_vorarlberg)
        MessageTest1.registerTranslation(MessagesTest1_xy)
    }


    /** Tests add new translations, fallback to default when translations are missing,
     *  setting of locale */
    @Test
    fun registerTranslationTest() {
        MessageTest1.unregisterAllTranslations()
        i18n4kConfig.locale = Locale("de")

        assertEquals("?YES?", MessageTest1.YES())

        MessageTest1.registerTranslation(MessagesTest1_en)

        assertEquals("Yes", MessageTest1.YES())

        MessageTest1.registerTranslation(MessagesTest1_de)

        assertEquals("Ja", MessageTest1.YES())

        i18n4kConfig.locale = Locale("xy")

        assertEquals("Yes", MessageTest1.YES())

        MessageTest1.registerTranslation(MessagesTest1_xy)

        assertEquals("Yes", MessageTest1.YES())

        i18n4kConfig.treadBlankStringAsNull = false

        assertEquals("", MessageTest1.YES())

        // "NO" has null value -> there for use default
        assertEquals("No", MessageTest1.NO())

    }

    @Test
    fun testSpecificLocales() {

        i18n4kConfig.locale = Locale("de")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = Locale("de", "AT")
        assertEquals("Servus Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = Locale("de", "AT", "vorarlberg")
        assertEquals("Zeawas Gerd!", MessageTest1.HELLO_X1("Gerd"))

        // go to less specific locale

        i18n4kConfig.locale = Locale("de", "AT", "tirol")
        assertEquals("Servus Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = Locale("de", "DE", "saxony")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = Locale("de", "DE")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = Locale("de")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = Locale("fr", "FR")
        assertEquals("Hello Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = Locale("fr")
        assertEquals("Hello Gerd!", MessageTest1.HELLO_X1("Gerd"))
    }


    @Test
    fun testMissingSpecificLocales() {
        i18n4kConfig.locale = Locale("de")
        assertEquals("Etwas Text 1", MessageTest1.SOME_ARE_NULL_1())

        i18n4kConfig.locale = Locale("de", "AT")
        assertEquals("Etwas Text 1 für AT", MessageTest1.SOME_ARE_NULL_1())

        i18n4kConfig.locale = Locale("de", "AT", "vorarlberg")
        assertEquals("Etwas Text 1 für AT", MessageTest1.SOME_ARE_NULL_1())


        i18n4kConfig.locale = Locale("de")
        assertEquals("Etwas Text 2", MessageTest1.SOME_ARE_NULL_2())

        i18n4kConfig.locale = Locale("de", "AT")
        assertEquals("Etwas Text 2", MessageTest1.SOME_ARE_NULL_2())

        i18n4kConfig.locale = Locale("de", "AT", "vorarlberg")
        assertEquals("Etwas Text 2", MessageTest1.SOME_ARE_NULL_2())


        i18n4kConfig.locale = Locale("de")
        assertEquals("?NO_TEXT?", MessageTest1.NO_TEXT())

        i18n4kConfig.locale = Locale("de", "AT")
        assertEquals("?NO_TEXT?", MessageTest1.NO_TEXT())

        i18n4kConfig.locale = Locale("de", "AT", "vorarlberg")
        assertEquals("?NO_TEXT?", MessageTest1.NO_TEXT())
    }

    /** Tests the setting of simple parameters. */
    @Test
    fun parameterTest() {
        assertEquals("Hello a!", MessageTest1.HELLO_X1("a"))
        assertEquals("Hello a & a!", MessageTest1.HELLO_X1_2("a"))
        assertEquals("Hello a and b!", MessageTest1.HELLO_X2("a", "b"))
        assertEquals("Hello a, b and c!", MessageTest1.HELLO_X3("a", "b", "c"))
        assertEquals("Hello a, b, c and d!", MessageTest1.HELLO_X4("a", "b", "c", "d"))
        assertEquals("Hello a, b, c, d and e!", MessageTest1.HELLO_X5("a", "b", "c", "d", "e"))
        assertEquals("Hello e, a, b, d and c!", MessageTest1.HELLO_X5_2("a", "b", "c", "d", "e"))
        assertEquals("Hello b, b, e, e and a!", MessageTest1.HELLO_X5_3("a", "b", "c", "d", "e"))
    }

    @Test
    fun getEntryByIdTest() {
        assertSame(
            MessageTest1.YES,
            MessageTest1.getEntryByKey(MessageTest1.YES.messageKey)
        )
        assertSame(
            MessageTest1.NO,
            MessageTest1.getEntryByKey(MessageTest1.NO.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X1,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X1.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X1_2,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X1_2.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X2,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X2.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X3,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X3.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X4,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X4.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X5,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X5.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X5_2,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X5_2.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X5_3,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X5_3.messageKey)
        )

    }

    @Test
    fun checkForDuplicateKey() {
        try {
            MessageTestDuplicateKey.getEntryByKey("YES")
            fail("Should fail!")
        } catch (e: Throwable) {
            // OK
        }
    }
}