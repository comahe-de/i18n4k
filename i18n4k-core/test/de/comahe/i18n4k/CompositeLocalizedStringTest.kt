package de.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.strings.plus
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CompositeLocalizedStringTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
        i18n4kConfig.locale = createLocale("en")

        MessageTest1.unregisterAllTranslations()
        MessageTest1.registerTranslation(MessagesTest1_en)
        MessageTest1.registerTranslation(MessagesTest1_de)
    }

    @Test
    fun testPlus() {
        val de = forLocaleTag("de")

        val two = MessageTest1.HELLO_X1["Adam"] + MessageTest1.YES
        val three = MessageTest1.HELLO_X1["Adam"] + MessageTest1.YES + MessageTest1.NO
        val withString = MessageTest1.HELLO_X1["Adam"] + " - " + MessageTest1.YES


        assertEquals("Hello Adam!Yes", two.toString())
        assertEquals("Hello Adam!YesNo", three.toString())
        assertEquals("Hello Adam! - Yes", withString.toString())

        assertEquals("Hallo Adam!Ja", two.toString(de))
        assertEquals("Hallo Adam!JaNein", three.toString(de))
        assertEquals("Hallo Adam! - Ja", withString.toString(de))
    }


}