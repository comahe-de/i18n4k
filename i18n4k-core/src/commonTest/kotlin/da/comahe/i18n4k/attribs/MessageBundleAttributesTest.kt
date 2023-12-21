package da.comahe.i18n4k.attribs

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
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

        MessagesThingsAre.unregisterAllTranslations()
        MessagesThingsAre.registerTranslation(MessagesThingsAre_en)
        MessagesThingsAre.registerTranslation(MessagesThingsAre_de)
    }

    @Test
    fun englishTest() {
        i18n4kConfig.locale = Locale("en")

        assertEquals(
            "The moon is beautiful. You will love it!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.MOON)
        )
        assertEquals(
            "The sun is beautiful. You will love it!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.SUN)
        )
        assertEquals(
            "The water is beautiful. You will love it!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.WATER)
        )
        assertEquals(
            "The Joseph is beautiful. You will love him!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.JOSEPH)
        )
        assertEquals(
            "The Mary is beautiful. You will love her!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.MARY)
        )

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
    }

    @Test
    fun germanTest() {
        i18n4kConfig.locale = Locale("de")

        assertEquals(
            "Der Mond ist schön. Du wirst ihn lieben!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.MOON)
        )
        assertEquals(
            "Die Sonne ist schön. Du wirst sie lieben!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.SUN)
        )
        assertEquals(
            "Das Wasser ist schön. Du wirst es lieben!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.WATER)
        )
        assertEquals(
            "Der Joseph ist schön. Du wirst ihn lieben!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.JOSEPH)
        )
        assertEquals(
            "Die Maria ist schön. Du wirst sie lieben!",
            MessagesThingsAre.X_IS_BEAUTIFUL(MessagesThings.MARY)
        )

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
}