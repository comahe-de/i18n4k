package de.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.messages.formatter.MessageParametersMap
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.fail

class MessageBundleTest {
    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
        i18n4kConfig.locale = createLocale("en")

        MessageTest1.unregisterAllTranslations()
        MessageTest1.registerTranslation(MessagesTest1_en)
        MessageTest1.registerTranslation(MessagesTest1_de)
        MessageTest1.registerTranslation(MessagesTest1_de_AT)
        MessageTest1.registerTranslation(MessagesTest1_de_AT_vorarlberg)
        MessageTest1.registerTranslation(MessagesTest1_xy)
    }


    /**
     * Tests add new translations, fallback to default when translations are missing, setting of
     * locale
     */
    @Test
    fun registerTranslationTest() {
        MessageTest1.unregisterAllTranslations()
        i18n4kConfig.locale = createLocale("de")

        assertEquals("?YES?", MessageTest1.YES())

        MessageTest1.registerTranslation(MessagesTest1_en)

        assertEquals("Yes", MessageTest1.YES())

        MessageTest1.registerTranslation(MessagesTest1_de)

        assertEquals("Ja", MessageTest1.YES())

        i18n4kConfig.locale = createLocale("xy")

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

        i18n4kConfig.locale = createLocale("de")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = createLocale("de", null, "AT")
        assertEquals("Servus Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = createLocale("de", null, "AT", "Voralberg")
        assertEquals("Zeawas Gerd!", MessageTest1.HELLO_X1("Gerd"))

        // go to less specific locale

        i18n4kConfig.locale = createLocale("de", null, "AT")
        assertEquals("Servus Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = createLocale("de", null, "DE", "saxony")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = createLocale("de", null,"DE")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = createLocale("de")
        assertEquals("Hallo Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = createLocale("fr", null, "FR")
        assertEquals("Hello Gerd!", MessageTest1.HELLO_X1("Gerd"))

        i18n4kConfig.locale = createLocale("fr")
        assertEquals("Hello Gerd!", MessageTest1.HELLO_X1("Gerd"))
    }


    @Test
    fun testMissingSpecificLocales() {
        i18n4kConfig.locale = createLocale("de")
        assertEquals("Etwas Text 1", MessageTest1.SOME_ARE_NULL_1())

        i18n4kConfig.locale = createLocale("de", null, "AT")
        assertEquals("Etwas Text 1 für AT", MessageTest1.SOME_ARE_NULL_1())

        i18n4kConfig.locale = createLocale("de", null, "AT")
        assertEquals("Etwas Text 1 für AT", MessageTest1.SOME_ARE_NULL_1())

        i18n4kConfig.locale = createLocale("de")
        assertEquals("Etwas Text 2", MessageTest1.SOME_ARE_NULL_2())

        i18n4kConfig.locale = createLocale("de")
        assertEquals("Etwas Text 2", MessageTest1.SOME_ARE_NULL_2())

        i18n4kConfig.locale = createLocale("de", null, "AT", "voralberg")
        assertEquals("Etwas Text 2", MessageTest1.SOME_ARE_NULL_2())


        i18n4kConfig.locale = createLocale("de")
        assertEquals("?NO_TEXT?", MessageTest1.NO_TEXT())

        i18n4kConfig.locale = createLocale("de", null, "AT")
        assertEquals("?NO_TEXT?", MessageTest1.NO_TEXT())

        i18n4kConfig.locale = createLocale("de", null, "AT", "voralberg")
        assertEquals("?NO_TEXT?", MessageTest1.NO_TEXT())
    }

    /** Tests the setting of simple parameters. */
    @Test
    fun parameterTest_toString() {
        // @formatter:off
        assertEquals("Hello a!", MessageTest1.HELLO_X1("a"))
        assertEquals("Hello a & a!", MessageTest1.HELLO_X1_2("a"))
        assertEquals("Hello a and b!", MessageTest1.HELLO_X2("a", "b"))
        assertEquals("Hello a, b and c!", MessageTest1.HELLO_X3("a", "b", "c"))
        assertEquals("Hello a, b, c and d!", MessageTest1.HELLO_X4("a", "b", "c", "d"))
        assertEquals("Hello a, b, c, d and e!", MessageTest1.HELLO_X5("a", "b", "c", "d", "e"))
        assertEquals("Hello a, b, c, d, e and f!", MessageTest1.HELLO_X6("a", "b", "c", "d", "e", "f"))
        assertEquals("Hello a, b, c, d, e, f and g!", MessageTest1.HELLO_X7("a", "b", "c", "d", "e", "f", "g"))
        assertEquals("Hello a, b, c, d, e, f, g and h!", MessageTest1.HELLO_X8("a", "b", "c", "d", "e", "f", "g", "h"))
        assertEquals("Hello a, b, c, d, e, f, g, h and i!", MessageTest1.HELLO_X9("a", "b", "c", "d", "e", "f", "g", "h", "i"))
        assertEquals("Hello a, b, c, d, e, f, g, h, i and j!", MessageTest1.HELLO_X10("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"))

        assertEquals("Hello e, a, b, d and c!", MessageTest1.HELLO_X5_2("a", "b", "c", "d", "e"))
        assertEquals("Hello b, b, e, e and a!", MessageTest1.HELLO_X5_3("a", "b", "c", "d", "e"))
        // @formatter:on
    }

    @Test
    fun parameterTest_toLocalizedString() {
        // @formatter:off
        assertEquals("Hello a!", MessageTest1.HELLO_X1["a"].toString())
        assertEquals("Hello a & a!", MessageTest1.HELLO_X1_2["a"].toString())
        assertEquals("Hello a and b!", MessageTest1.HELLO_X2["a", "b"].toString())
        assertEquals("Hello a, b and c!", MessageTest1.HELLO_X3["a", "b", "c"].toString())
        assertEquals("Hello a, b, c and d!", MessageTest1.HELLO_X4["a", "b", "c", "d"].toString())
        assertEquals("Hello a, b, c, d and e!", MessageTest1.HELLO_X5["a", "b", "c", "d", "e"].toString())
        assertEquals("Hello a, b, c, d, e and f!", MessageTest1.HELLO_X6["a", "b", "c", "d", "e", "f"].toString())
        assertEquals("Hello a, b, c, d, e, f and g!", MessageTest1.HELLO_X7["a", "b", "c", "d", "e", "f", "g"].toString())
        assertEquals("Hello a, b, c, d, e, f, g and h!", MessageTest1.HELLO_X8["a", "b", "c", "d", "e", "f", "g", "h"].toString())
        assertEquals("Hello a, b, c, d, e, f, g, h and i!", MessageTest1.HELLO_X9["a", "b", "c", "d", "e", "f", "g", "h", "i"].toString())
        assertEquals("Hello a, b, c, d, e, f, g, h, i and j!", MessageTest1.HELLO_X10["a", "b", "c", "d", "e", "f", "g", "h", "i", "j"].toString())

        assertEquals("Hello e, a, b, d and c!", MessageTest1.HELLO_X5_2["a", "b", "c", "d", "e"].toString())
        assertEquals("Hello b, b, e, e and a!", MessageTest1.HELLO_X5_3["a", "b", "c", "d", "e"].toString())
        // @formatter:on
    }

    /** Tests the setting of simple parameters. */
    @Test
    fun parameterTest_toString_locale() {
        val l = createLocale("de")
        // @formatter:off
        assertEquals("Hallo a!", MessageTest1.HELLO_X1("a",l))
        assertEquals("Hallo a & a!", MessageTest1.HELLO_X1_2("a",l))
        assertEquals("Hallo a und b!", MessageTest1.HELLO_X2("a", "b",l))
        assertEquals("Hallo a, b und c!", MessageTest1.HELLO_X3("a", "b", "c",l))
        assertEquals("Hallo a, b, c und d!", MessageTest1.HELLO_X4("a", "b", "c", "d",l))
        assertEquals("Hallo a, b, c, d und e!", MessageTest1.HELLO_X5("a", "b", "c", "d", "e",l))
        assertEquals("Hallo a, b, c, d, e und f!", MessageTest1.HELLO_X6("a", "b", "c", "d", "e", "f",l))
        assertEquals("Hallo a, b, c, d, e, f und g!", MessageTest1.HELLO_X7("a", "b", "c", "d", "e", "f", "g",l))
        assertEquals("Hallo a, b, c, d, e, f, g und h!", MessageTest1.HELLO_X8("a", "b", "c", "d", "e", "f", "g", "h",l))
        assertEquals("Hallo a, b, c, d, e, f, g, h und i!", MessageTest1.HELLO_X9("a", "b", "c", "d", "e", "f", "g", "h", "i",l))
        assertEquals("Hallo a, b, c, d, e, f, g, h, i und j!", MessageTest1.HELLO_X10("a", "b", "c", "d", "e", "f", "g", "h", "i", "j",l))

        assertEquals("Hallo e, a, b, d und c!", MessageTest1.HELLO_X5_2("a", "b", "c", "d", "e",l))
        assertEquals("Hallo b, b, e, e und a!", MessageTest1.HELLO_X5_3("a", "b", "c", "d", "e",l))
        // @formatter:on
    }

    @Test
    fun parameterTest_toLocalizedString_locale() {
        val l = createLocale("de")
        // @formatter:off
        assertEquals("Hallo a!", MessageTest1.HELLO_X1["a"].toString(l))
        assertEquals("Hallo a & a!", MessageTest1.HELLO_X1_2["a"].toString(l))
        assertEquals("Hallo a und b!", MessageTest1.HELLO_X2["a", "b"].toString(l))
        assertEquals("Hallo a, b und c!", MessageTest1.HELLO_X3["a", "b", "c"].toString(l))
        assertEquals("Hallo a, b, c und d!", MessageTest1.HELLO_X4["a", "b", "c", "d"].toString(l))
        assertEquals("Hallo a, b, c, d und e!", MessageTest1.HELLO_X5["a", "b", "c", "d", "e"].toString(l))
        assertEquals("Hallo a, b, c, d, e und f!", MessageTest1.HELLO_X6["a", "b", "c", "d", "e", "f"].toString(l))
        assertEquals("Hallo a, b, c, d, e, f und g!", MessageTest1.HELLO_X7["a", "b", "c", "d", "e", "f", "g"].toString(l))
        assertEquals("Hallo a, b, c, d, e, f, g und h!", MessageTest1.HELLO_X8["a", "b", "c", "d", "e", "f", "g", "h"].toString(l))
        assertEquals("Hallo a, b, c, d, e, f, g, h und i!", MessageTest1.HELLO_X9["a", "b", "c", "d", "e", "f", "g", "h", "i"].toString(l))
        assertEquals("Hallo a, b, c, d, e, f, g, h, i und j!", MessageTest1.HELLO_X10["a", "b", "c", "d", "e", "f", "g", "h", "i", "j"].toString(l))

        assertEquals("Hallo e, a, b, d und c!", MessageTest1.HELLO_X5_2["a", "b", "c", "d", "e"].toString(l))
        assertEquals("Hallo b, b, e, e und a!", MessageTest1.HELLO_X5_3["a", "b", "c", "d", "e"].toString(l))
        // @formatter:on
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
        assertSame(
            MessageTest1.HELLO_X6,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X6.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X7,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X7.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X8,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X8.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X9,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X9.messageKey)
        )
        assertSame(
            MessageTest1.HELLO_X10,
            MessageTest1.getEntryByKey(MessageTest1.HELLO_X10.messageKey)
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

    @Test
    fun selectPatternTest() {
        assertEquals(
            "Peter has forgotten his bag.",
            MessageTest1.SELECT_PATTERN("Peter", "male", "1", "one")
        )
        assertEquals(
            "Peter has forgotten his 3 bags.",
            MessageTest1.SELECT_PATTERN("Peter", "male", "3", "few")
        )
        assertEquals(
            "Sara has forgotten her bag.",
            MessageTest1.SELECT_PATTERN("Sara", "female", "1", "one")
        )
        assertEquals(
            "Sara has forgotten her 3 bags.",
            MessageTest1.SELECT_PATTERN("Sara", "female", "3", "few")
        )

        val de = createLocale("de")
        assertEquals(
            "Peter hat seine Tasche vergessen.",
            MessageTest1.SELECT_PATTERN("Peter", "male", "1", "one", de)
        )
        assertEquals(
            "Peter hat seine 3 Taschen vergessen.",
            MessageTest1.SELECT_PATTERN("Peter", "male", "3", "few", de)
        )
        assertEquals(
            "Sara hat ihre Tasche vergessen.",
            MessageTest1.SELECT_PATTERN("Sara", "female", "1", "one", de)
        )
        assertEquals(
            "Sara hat ihre 3 Taschen vergessen.",
            MessageTest1.SELECT_PATTERN("Sara", "female", "3", "few", de)
        )
    }

    @Test
    fun numberPatternTest() {
        assertEquals(
            "It's 1,234.57!",
            MessageTest1.NUMBER_PATTERN(1234.56789)
        )
        val de = createLocale("de")
        assertEquals(
            "Es ist 1.234,57!",
            MessageTest1.NUMBER_PATTERN(1234.56789, de)
        )
    }

    /** See bug: Double single quotes are not escaped in string without parameters #59 */
    @Test
    fun singedQuotesTest() {
        assertEquals(
            "It's a qute! Look: '",
            MessageTest1.SINGLE_QUOTES()
        )
        val de = createLocale("de")
        assertEquals(
            "Schau's dir an! Ein Hochkomma: '",
            MessageTest1.SINGLE_QUOTES(de)
        )
    }

    @Test
    fun namedParametersTest() {
        assertEquals("Hello a!", MessageTest1.HELLO_X1_NAMED("a"))
        assertEquals(
            "Hello a!", MessageTest1.HELLO_X1_NAMED.asN(
                MessageParametersMap("pa" to "a", "pb" to "b")
            )
        )
        assertEquals(
            "Hello a!",
            MessageTest1.HELLO_X1_NAMED.asN("pa" to "a", "pb" to "b")
        )
        assertEquals(
            "Hello a!",
            MessageTest1.HELLO_X1_NAMED.asN["pa" to "a", "pb" to "b"].toString()
        )

        assertEquals("Hello a and b!", MessageTest1.HELLO_X2_NAMED("a", "b"))
        assertEquals(
            "Hello a and b!", MessageTest1.HELLO_X2_NAMED.asN(
                MessageParametersMap("pa" to "a", "pb" to "b")
            )
        )
        assertEquals(
            "Hello a and b!",
            MessageTest1.HELLO_X2_NAMED.asN("pa" to "a", "pb" to "b")
        )
        assertEquals(
            "Hello a and b!",
            MessageTest1.HELLO_X2_NAMED.asN["pa" to "a", "pb" to "b"].toString()
        )
        ////////////////////////////////////////
        val de = forLocaleTag("de")

        assertEquals("Hallo a!", MessageTest1.HELLO_X1_NAMED("a", de))
        assertEquals(
            "Hallo a!", MessageTest1.HELLO_X1_NAMED.asN(
                MessageParametersMap("pa" to "a"), de
            )
        )
        assertEquals(
            "Hallo a!",
            MessageTest1.HELLO_X1_NAMED.asN("pa" to "a", locale = de)
        )
        assertEquals(
            "Hallo a!",
            MessageTest1.HELLO_X1_NAMED.asN["pa" to "a"].toString(de)
        )

        assertEquals("Hallo a und b!", MessageTest1.HELLO_X2_NAMED("a", "b", de))
        assertEquals(
            "Hallo a und b!", MessageTest1.HELLO_X2_NAMED.asN(
                MessageParametersMap("pa" to "a", "pb" to "b"), de
            )
        )
        assertEquals(
            "Hallo a und b!",
            MessageTest1.HELLO_X2_NAMED.asN("pa" to "a", "pb" to "b", locale = de)
        )
        assertEquals(
            "Hallo a und b!",
            MessageTest1.HELLO_X2_NAMED.asN["pa" to "a", "pb" to "b"].toString(de)
        )
    }

    /** Tests the setting of simple parameters. */
    @Test
    fun parameterTest_named_toString() {
        // @formatter:off
        assertEquals("Hello a!", MessageTest1.HELLO_X1_NAMED("a"))
        assertEquals("Hello a & a!", MessageTest1.HELLO_X1_2_NAMED("a"))
        assertEquals("Hello a and b!", MessageTest1.HELLO_X2_NAMED("a", "b"))
        assertEquals("Hello a, b and c!", MessageTest1.HELLO_X3_NAMED("a", "b", "c"))
        assertEquals("Hello a, b, c and d!", MessageTest1.HELLO_X4_NAMED("a", "b", "c", "d"))
        assertEquals("Hello a, b, c, d and e!", MessageTest1.HELLO_X5_NAMED("a", "b", "c", "d", "e"))
        assertEquals("Hello a, b, c, d, e and f!", MessageTest1.HELLO_X6_NAMED("a", "b", "c", "d", "e", "f"))
        assertEquals("Hello a, b, c, d, e, f and g!", MessageTest1.HELLO_X7_NAMED("a", "b", "c", "d", "e", "f", "g"))
        assertEquals("Hello a, b, c, d, e, f, g and h!", MessageTest1.HELLO_X8_NAMED("a", "b", "c", "d", "e", "f", "g", "h"))
        assertEquals("Hello a, b, c, d, e, f, g, h and i!", MessageTest1.HELLO_X9_NAMED("a", "b", "c", "d", "e", "f", "g", "h", "i"))
        assertEquals("Hello a, b, c, d, e, f, g, h, i and j!", MessageTest1.HELLO_X10_NAMED("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"))

        assertEquals("Hello e, a, b, d and c!", MessageTest1.HELLO_X5_2_NAMED("a", "b", "c", "d", "e"))
        assertEquals("Hello b, b, e, e and a!", MessageTest1.HELLO_X5_3_NAMED("a", "b", "c", "d", "e"))
        // @formatter:on
    }

    @Test
    fun parameterTest_named_toLocalizedString() {
        // @formatter:off
        assertEquals("Hello a!", MessageTest1.HELLO_X1_NAMED["a"].toString())
        assertEquals("Hello a & a!", MessageTest1.HELLO_X1_2_NAMED["a"].toString())
        assertEquals("Hello a and b!", MessageTest1.HELLO_X2_NAMED["a", "b"].toString())
        assertEquals("Hello a, b and c!", MessageTest1.HELLO_X3_NAMED["a", "b", "c"].toString())
        assertEquals("Hello a, b, c and d!", MessageTest1.HELLO_X4_NAMED["a", "b", "c", "d"].toString())
        assertEquals("Hello a, b, c, d and e!", MessageTest1.HELLO_X5_NAMED["a", "b", "c", "d", "e"].toString())
        assertEquals("Hello a, b, c, d, e and f!", MessageTest1.HELLO_X6_NAMED["a", "b", "c", "d", "e", "f"].toString())
        assertEquals("Hello a, b, c, d, e, f and g!", MessageTest1.HELLO_X7_NAMED["a", "b", "c", "d", "e", "f", "g"].toString())
        assertEquals("Hello a, b, c, d, e, f, g and h!", MessageTest1.HELLO_X8_NAMED["a", "b", "c", "d", "e", "f", "g", "h"].toString())
        assertEquals("Hello a, b, c, d, e, f, g, h and i!", MessageTest1.HELLO_X9_NAMED["a", "b", "c", "d", "e", "f", "g", "h", "i"].toString())
        assertEquals("Hello a, b, c, d, e, f, g, h, i and j!", MessageTest1.HELLO_X10_NAMED["a", "b", "c", "d", "e", "f", "g", "h", "i", "j"].toString())

        assertEquals("Hello e, a, b, d and c!", MessageTest1.HELLO_X5_2_NAMED["a", "b", "c", "d", "e"].toString())
        assertEquals("Hello b, b, e, e and a!", MessageTest1.HELLO_X5_3_NAMED["a", "b", "c", "d", "e"].toString())
        // @formatter:on       // @formatter:on
    }
}