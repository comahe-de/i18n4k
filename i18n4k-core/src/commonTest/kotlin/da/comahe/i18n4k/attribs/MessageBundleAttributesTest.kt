package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProvider
import de.comahe.i18n4k.messages.formatter.provider.GenderProvider
import de.comahe.i18n4k.strings.LocalizedAttributeMap
import de.comahe.i18n4k.strings.LocalizedStringMap
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageBundleAttributesTest {
    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
        i18n4kConfig.locale = Locale("en")

        MessagesThings.unregisterAllTranslations()
        MessagesThings.registerTranslation(MessagesThings_en)
        MessagesThings.registerTranslation(MessagesThings_de)
        MessagesThings.registerTranslation(MessagesThings_en_x_attr_gender)
        MessagesThings.registerTranslation(MessagesThings_de_x_attr_gender)
        MessagesThings.registerTranslation(MessagesThings_de_x_attr_decl_genitiv)
        MessagesThings.registerTranslation(MessagesThings_x_attr_common)

        MessagesThingsAre.unregisterAllTranslations()
        MessagesThingsAre.registerTranslation(MessagesThingsAre_en)
        MessagesThingsAre.registerTranslation(MessagesThingsAre_de)
    }

    @Test
    fun getAttributeTest() {
        i18n4kConfig.locale = forLocaleTag("en")

        assertEquals("n", MessagesThings.MOON.getAttribute("gender"))
        assertEquals("n", MessagesThings.SUN.getAttribute("gender"))
        assertEquals("n", MessagesThings.WATER.getAttribute("gender"))
        assertEquals("m", MessagesThings.JOSEPH.getAttribute("gender"))
        assertEquals("f", MessagesThings.MARY.getAttribute("gender"))


        i18n4kConfig.locale = forLocaleTag("de")

        assertEquals("m", MessagesThings.MOON.getAttribute("gender"))
        assertEquals("f", MessagesThings.SUN.getAttribute("gender"))
        assertEquals("n", MessagesThings.WATER.getAttribute("gender"))
        assertEquals("m", MessagesThings.JOSEPH.getAttribute("gender"))
        assertEquals("f", MessagesThings.MARY.getAttribute("gender"))
    }

    @Test
    fun getAttributeCommonTest() {
        i18n4kConfig.locale = forLocaleTag("en")

        assertEquals("X1", MessagesThings.MOON.getAttribute("common"))
        assertEquals("X2", MessagesThings.SUN.getAttribute("common"))
        assertEquals("X3", MessagesThings.WATER.getAttribute("common"))
        assertEquals("X4", MessagesThings.JOSEPH.getAttribute("common"))
        assertEquals("X5", MessagesThings.MARY.getAttribute("common"))


        i18n4kConfig.locale = forLocaleTag("de")

        assertEquals("X1", MessagesThings.MOON.getAttribute("common"))
        assertEquals("X2", MessagesThings.SUN.getAttribute("common"))
        assertEquals("X3", MessagesThings.WATER.getAttribute("common"))
        assertEquals("X4", MessagesThings.JOSEPH.getAttribute("common"))
        assertEquals("X5", MessagesThings.MARY.getAttribute("common"))
    }

    @Test
    fun englishTest() {
        i18n4kConfig.locale = Locale("en")

        for (localizedStringFactory1 in arrayOf(
            MessagesThingsAre.X_IS_BEAUTIFUL,
            MessagesThingsAre.X_IS_BEAUTIFUL_2
        )) {
            assertEquals(
                "The moon is beautiful. You will love it!",
                localizedStringFactory1(MessagesThings.MOON)
            )
            assertEquals(
                "The sun is beautiful. You will love it!",
                localizedStringFactory1(MessagesThings.SUN)
            )
            assertEquals(
                "The water is beautiful. You will love it!",
                localizedStringFactory1(MessagesThings.WATER)
            )
            assertEquals(
                "The Joseph is beautiful. You will love him!",
                localizedStringFactory1(MessagesThings.JOSEPH)
            )
            assertEquals(
                "The Mary is beautiful. You will love her!",
                localizedStringFactory1(MessagesThings.MARY)
            )
        }

        assertEquals(
            "Mary has gender f",
            MessagesThingsAre.X_HAS_GENDER(MessagesThings.MARY)
        )
        assertEquals(
            "Joseph has gender m",
            MessagesThingsAre.X_HAS_GENDER(MessagesThings.JOSEPH)
        )
        assertEquals(
            "water has gender n",
            MessagesThingsAre.X_HAS_GENDER(MessagesThings.WATER)
        )

        assertEquals(
            "The color of the sun.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.SUN)
        )
    }

    @Test
    fun germanTest() {
        i18n4kConfig.locale = Locale("de")

        for (localizedStringFactory1 in arrayOf(
            MessagesThingsAre.X_IS_BEAUTIFUL,
            MessagesThingsAre.X_IS_BEAUTIFUL_2,
        )) {
            assertEquals(
                "Der Mond ist schön. Du wirst ihn lieben!",
                localizedStringFactory1(MessagesThings.MOON)
            )
            assertEquals(
                "Die Sonne ist schön. Du wirst sie lieben!",
                localizedStringFactory1(MessagesThings.SUN)
            )
            assertEquals(
                "Das Wasser ist schön. Du wirst es lieben!",
                localizedStringFactory1(MessagesThings.WATER)
            )
            assertEquals(
                "Der Joseph ist schön. Du wirst ihn lieben!",
                localizedStringFactory1(MessagesThings.JOSEPH)
            )
            assertEquals(
                "Die Maria ist schön. Du wirst sie lieben!",
                localizedStringFactory1(MessagesThings.MARY)
            )
        }

        assertEquals(
            "Maria hat Geschlecht f",
            MessagesThingsAre.X_HAS_GENDER(MessagesThings.MARY)
        )
        assertEquals(
            "Joseph hat Geschlecht m",
            MessagesThingsAre.X_HAS_GENDER(MessagesThings.JOSEPH)
        )
        assertEquals(
            "Wasser hat Geschlecht n",
            MessagesThingsAre.X_HAS_GENDER(MessagesThings.WATER)
        )

        assertEquals(
            "Die Farbe des Mondes.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.MOON)
        )
        assertEquals(
            "Die Farbe der Sonne.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.SUN)
        )
        assertEquals(
            "Die Farbe des Wassers.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.WATER)
        )
        assertEquals(
            "Die Farbe des Josephs.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.JOSEPH)
        )
        assertEquals(
            "Die Farbe der Maria.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.MARY)
        )
    }

    @Test
    fun notExistingAttributeTest() {
        i18n4kConfig.locale = Locale("en")

        assertEquals(
            "Not existing: Mary",
            MessagesThingsAre.NOT_EXISTING_ATTR(MessagesThings.MARY)
        )
        assertEquals(
            "Not existing with default: foo!",
            MessagesThingsAre.NOT_EXISTING_ATTR_WITH_DEFAULT(MessagesThings.MARY, "foo")
        )

        i18n4kConfig.locale = Locale("de")

        assertEquals(
            "Nicht existent: Maria",
            MessagesThingsAre.NOT_EXISTING_ATTR(MessagesThings.MARY)
        )
        assertEquals(
            "Nicht existent mit Default: foo!",
            MessagesThingsAre.NOT_EXISTING_ATTR_WITH_DEFAULT(MessagesThings.MARY, "foo")
        )
    }

    /** Test setting a custom gender provider */
    @Test
    fun customGenderProviderTest() {
        i18n4kConfig.locale = Locale("en")

        //everything is "n"
        i18n4kConfig.genderProvider = object : GenderProvider {
            override fun getGenderOf(value: Any?, locale: Locale?): String? {
                return "n"
            }
        }
        assertEquals(
            "The water is beautiful. You will love it!",
            MessagesThingsAre.X_IS_BEAUTIFUL_2(MessagesThings.WATER)
        )
        assertEquals(
            "The Joseph is beautiful. You will love it!",
            MessagesThingsAre.X_IS_BEAUTIFUL_2(MessagesThings.JOSEPH)
        )
        assertEquals(
            "The Mary is beautiful. You will love it!",
            MessagesThingsAre.X_IS_BEAUTIFUL_2(MessagesThings.MARY)
        )
    }

    /** Test setting a custom gender provider */
    @Test
    fun customDeclensionProviderTest() {
        i18n4kConfig.locale = Locale("de")

        //everything is "n"
        i18n4kConfig.declensionProvider = object : DeclensionProvider {
            override fun getDeclensionOf(
                declensionCase: CharSequence,
                value: Any?,
                locale: Locale?
            ): String? {
                return value.toString() + "-o"
            }

        }
        assertEquals(
            "Die Farbe des Mond-o.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.MOON)
        )
        assertEquals(
            "Die Farbe der Sonne-o.",
            MessagesThingsAre.THE_COLOR_OF_X(MessagesThings.SUN)
        )
    }

    @Test
    fun attributableTest() {
        val en = forLocaleTag("en")
        val de = forLocaleTag("de")
        val fr = forLocaleTag("fr")

        val attributeMap = LocalizedAttributeMap(
            Triple("none", en, "nothing"),
            Triple("none", de, "nichts"),
            Triple("none2", en, "empty"),
            Triple("none2", de, "leer"),
        )

        i18n4kConfig.locale = en

        assertEquals(
            "Not existing: nothing",
            MessagesThingsAre.NOT_EXISTING_ATTR(attributeMap)
        )
        assertEquals(
            "Not existing 2: empty",
            MessagesThingsAre.NOT_EXISTING_ATTR2(attributeMap)
        )

        i18n4kConfig.locale = fr

        assertEquals(
            "Not existing: nothing",
            MessagesThingsAre.NOT_EXISTING_ATTR(attributeMap)
        )
        assertEquals(
            "Not existing 2: empty",
            MessagesThingsAre.NOT_EXISTING_ATTR2(attributeMap)
        )

        i18n4kConfig.locale = de

        assertEquals(
            "Nicht existent: nichts",
            MessagesThingsAre.NOT_EXISTING_ATTR(attributeMap)
        )
        assertEquals(
            "Nicht existent 2: leer",
            MessagesThingsAre.NOT_EXISTING_ATTR2(attributeMap)
        )
    }
}