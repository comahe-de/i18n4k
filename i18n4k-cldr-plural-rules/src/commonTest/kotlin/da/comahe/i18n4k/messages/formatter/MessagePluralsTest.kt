package da.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.i18n4kInitCldrPluralRules
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessagePluralsTest {
    private var i18n4kConfig = I18n4kConfigDefault()

    init {
        i18n4kInitCldrPluralRules()
    }

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
        i18n4kConfig.locale = Locale("en")

        MessagesPlurals.unregisterAllTranslations()
        MessagesPlurals.registerTranslation(MessagesPlurals_en)
        MessagesPlurals.registerTranslation(MessagesPlurals_de)
        MessagesPlurals.registerTranslation(MessagesPlurals_pl)

    }

    @Test
    fun cardinalTest_en() {
        i18n4kConfig.locale = Locale("en")

        assertEquals("1 month", MessagesPlurals.MONTH_COUNT("1"))
        assertEquals("1 month", MessagesPlurals.MONTH_COUNT(1))
        assertEquals("2 months", MessagesPlurals.MONTH_COUNT("2"))
        assertEquals("2 months", MessagesPlurals.MONTH_COUNT(2))
        assertEquals("3 months", MessagesPlurals.MONTH_COUNT("3"))
        assertEquals("3 months", MessagesPlurals.MONTH_COUNT(3))
        assertEquals("5 months", MessagesPlurals.MONTH_COUNT("5"))
        assertEquals("5 months", MessagesPlurals.MONTH_COUNT(5))
        assertEquals("0 months", MessagesPlurals.MONTH_COUNT("0"))
        assertEquals("0 months", MessagesPlurals.MONTH_COUNT(0))
        assertEquals("1.5 months", MessagesPlurals.MONTH_COUNT("1.5"))
        assertEquals("1.5 months", MessagesPlurals.MONTH_COUNT(1.5))

        assertEquals("x months", MessagesPlurals.MONTH_COUNT("x"))
    }

    @Test
    fun cardinalTest_de() {
        i18n4kConfig.locale = Locale("de")

        assertEquals("1 Monat", MessagesPlurals.MONTH_COUNT("1"))
        assertEquals("1 Monat", MessagesPlurals.MONTH_COUNT(1))
        assertEquals("2 Monate", MessagesPlurals.MONTH_COUNT("2"))
        assertEquals("2 Monate", MessagesPlurals.MONTH_COUNT(2))
        assertEquals("3 Monate", MessagesPlurals.MONTH_COUNT("3"))
        assertEquals("3 Monate", MessagesPlurals.MONTH_COUNT(3))
        assertEquals("5 Monate", MessagesPlurals.MONTH_COUNT("5"))
        assertEquals("5 Monate", MessagesPlurals.MONTH_COUNT(5))
        assertEquals("0 Monate", MessagesPlurals.MONTH_COUNT("0"))
        assertEquals("0 Monate", MessagesPlurals.MONTH_COUNT(0))
        assertEquals("1.5 Monate", MessagesPlurals.MONTH_COUNT("1.5"))
        assertEquals("1.5 Monate", MessagesPlurals.MONTH_COUNT(1.5))

        assertEquals("x Monate", MessagesPlurals.MONTH_COUNT("x"))
    }

    @Test
    fun cardinalTest_pl() {
        i18n4kConfig.locale = Locale("pl")

        assertEquals("1 miesiąc", MessagesPlurals.MONTH_COUNT("1"))
        assertEquals("1 miesiąc", MessagesPlurals.MONTH_COUNT(1))
        assertEquals("2 miesiące", MessagesPlurals.MONTH_COUNT("2"))
        assertEquals("2 miesiące", MessagesPlurals.MONTH_COUNT(2))
        assertEquals("3 miesiące", MessagesPlurals.MONTH_COUNT("3"))
        assertEquals("3 miesiące", MessagesPlurals.MONTH_COUNT(3))
        assertEquals("5 miesięcy", MessagesPlurals.MONTH_COUNT("5"))
        assertEquals("5 miesięcy", MessagesPlurals.MONTH_COUNT(5))
        assertEquals("0 miesięcy", MessagesPlurals.MONTH_COUNT("0"))
        assertEquals("0 miesięcy", MessagesPlurals.MONTH_COUNT(0))
        assertEquals("1.5 miesiąca", MessagesPlurals.MONTH_COUNT("1.5"))
        assertEquals("1.5 miesiąca", MessagesPlurals.MONTH_COUNT(1.5))

        assertEquals("x miesiąca", MessagesPlurals.MONTH_COUNT("x"))
    }

    @Test
    fun ordinalTest_en() {
        i18n4kConfig.locale = Locale("en")

        assertEquals("1st rank", MessagesPlurals.RANK_ORDINAL("1"))
        assertEquals("1st rank", MessagesPlurals.RANK_ORDINAL(1))
        assertEquals("2nd rank", MessagesPlurals.RANK_ORDINAL("2"))
        assertEquals("2nd rank", MessagesPlurals.RANK_ORDINAL(2))
        assertEquals("3rd rank", MessagesPlurals.RANK_ORDINAL("3"))
        assertEquals("3rd rank", MessagesPlurals.RANK_ORDINAL(3))
        assertEquals("5th rank", MessagesPlurals.RANK_ORDINAL("5"))
        assertEquals("5th rank", MessagesPlurals.RANK_ORDINAL(5))
        assertEquals("0th rank", MessagesPlurals.RANK_ORDINAL("0"))
        assertEquals("0th rank", MessagesPlurals.RANK_ORDINAL(0))
        assertEquals("1.5th rank", MessagesPlurals.RANK_ORDINAL("1.5"))
        assertEquals("1.5th rank", MessagesPlurals.RANK_ORDINAL(1.5))

        assertEquals("xth rank", MessagesPlurals.RANK_ORDINAL("x"))

    }

    @Test
    fun ordinalTest_de() {
        i18n4kConfig.locale = Locale("de")

        assertEquals("1. Rang", MessagesPlurals.RANK_ORDINAL("1"))
        assertEquals("1. Rang", MessagesPlurals.RANK_ORDINAL(1))
        assertEquals("2. Rang", MessagesPlurals.RANK_ORDINAL("2"))
        assertEquals("2. Rang", MessagesPlurals.RANK_ORDINAL(2))
        assertEquals("3. Rang", MessagesPlurals.RANK_ORDINAL("3"))
        assertEquals("3. Rang", MessagesPlurals.RANK_ORDINAL(3))
        assertEquals("5. Rang", MessagesPlurals.RANK_ORDINAL("5"))
        assertEquals("5. Rang", MessagesPlurals.RANK_ORDINAL(5))
        assertEquals("0. Rang", MessagesPlurals.RANK_ORDINAL("0"))
        assertEquals("0. Rang", MessagesPlurals.RANK_ORDINAL(0))
        assertEquals("1.5. Rang", MessagesPlurals.RANK_ORDINAL("1.5"))
        assertEquals("1.5. Rang", MessagesPlurals.RANK_ORDINAL(1.5))

        assertEquals("x. Rang", MessagesPlurals.RANK_ORDINAL("x"))

    }

    @Test
    fun ordinalTest_pl() {
        i18n4kConfig.locale = Locale("pl")

        assertEquals("1. rząd", MessagesPlurals.RANK_ORDINAL("1"))
        assertEquals("1. rząd", MessagesPlurals.RANK_ORDINAL(1))
        assertEquals("2. rząd", MessagesPlurals.RANK_ORDINAL("2"))
        assertEquals("2. rząd", MessagesPlurals.RANK_ORDINAL(2))
        assertEquals("3. rząd", MessagesPlurals.RANK_ORDINAL("3"))
        assertEquals("3. rząd", MessagesPlurals.RANK_ORDINAL(3))
        assertEquals("5. rząd", MessagesPlurals.RANK_ORDINAL("5"))
        assertEquals("5. rząd", MessagesPlurals.RANK_ORDINAL(5))
        assertEquals("0. rząd", MessagesPlurals.RANK_ORDINAL("0"))
        assertEquals("0. rząd", MessagesPlurals.RANK_ORDINAL(0))
        assertEquals("1.5. rząd", MessagesPlurals.RANK_ORDINAL("1.5"))
        assertEquals("1.5. rząd", MessagesPlurals.RANK_ORDINAL(1.5))

        assertEquals("x. rząd", MessagesPlurals.RANK_ORDINAL("x"))

    }
}