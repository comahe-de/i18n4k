package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locales
import de.comahe.i18n4k.MessageTest1
import de.comahe.i18n4k.MessagesTest1_de
import de.comahe.i18n4k.MessagesTest1_en
import de.comahe.i18n4k.attribs.MessagesThings
import de.comahe.i18n4k.attribs.MessagesThings_de
import de.comahe.i18n4k.attribs.MessagesThings_en
import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.i18n4k
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizedStringBuilderTest {

    val config = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = config

        MessageTest1.registerTranslation(MessagesTest1_en)
        MessageTest1.registerTranslation(MessagesTest1_de)
        MessagesThings.registerTranslation(MessagesThings_en)
        MessagesThings.registerTranslation(MessagesThings_de)
    }

    @Test
    fun testEmpty() {
        val builder = LocalizedStringBuilder()

        assertEquals("", builder.toString())
        assertEquals("", builder.build().toString())

        builder.prefix = "<".asLocalizedString()

        assertEquals("<", builder.toString())
        assertEquals("<", builder.build().toString())

        builder.postfix = ">".asLocalizedString()

        assertEquals("<>", builder.toString())
        assertEquals("<>", builder.build().toString())

        builder.prefix = null

        assertEquals(">", builder.toString())
        assertEquals(">", builder.build().toString())

        builder.separator = ",".asLocalizedString()

        assertEquals(">", builder.toString())
        assertEquals(">", builder.build().toString())
    }


    @Test
    fun testParametersAreLocalizedStrings() {
        val builder = LocalizedStringBuilder()

        builder.prefix = MessagesThings.SUN
        builder.postfix = MessagesThings.MOON
        builder.separator = MessagesThings.MARY
        builder.truncated = MessagesThings.JOSEPH

        builder += MessageTest1.YES
        builder += MessageTest1.NO

        config.locale = Locales.ENGLISH
        assertEquals("sunYesMaryNomoon", builder.toString())
        assertEquals("sunYesMaryNomoon", builder.build().toString())
        config.locale = Locales.GERMAN
        assertEquals("SonneJaMariaNeinMond", builder.toString())
        assertEquals("SonneJaMariaNeinMond", builder.build().toString())

        builder.limit = 1

        config.locale = Locales.ENGLISH
        assertEquals("sunYesMaryJosephmoon", builder.toString())
        assertEquals("sunYesMaryJosephmoon", builder.build().toString())
        config.locale = Locales.GERMAN
        assertEquals("SonneJaMariaJosephMond", builder.toString())
        assertEquals("SonneJaMariaJosephMond", builder.build().toString())

    }


    @Test
    fun testCharSequence() {
        val builder = LocalizedStringBuilder()

        builder += "Hi, "
        builder += MessagesThings.MARY
        builder += StringBuilder(" and ")
        builder += MessagesThings.JOSEPH
        builder += "!"


        config.locale = Locales.ENGLISH
        assertEquals("Hi, Mary and Joseph!", builder.toString())
        assertEquals("Hi, Mary and Joseph!", builder.build().toString())

        config.locale = Locales.GERMAN
        assertEquals("Hi, Maria and Joseph!", builder.toString())
        assertEquals("Hi, Maria and Joseph!", builder.build().toString())
    }

    @Test
    fun testBuild() {

        val builder = LocalizedStringBuilder()

        val s0 = builder.build()

        builder += MessageTest1.YES
        val s1 = builder.build()

        builder += MessageTest1.NO
        val s2 = builder.build()

        builder.separator = ", ".asLocalizedString()
        val s3 = builder.build()

        builder.prefix = "<".asLocalizedString()
        val s4 = builder.build()

        builder.postfix = ">".asLocalizedString()
        val s5 = builder.build()

        builder.limit = 1
        val s6 = builder.build()

        builder.truncated = "~".asLocalizedString()
        val s7 = builder.build()

        builder.limit = 2
        val s8 = builder.build()

        builder.limit = 0
        val s9 = builder.build()

        config.locale = Locales.ENGLISH
        assertEquals("", s0())
        assertEquals("Yes", s1())
        assertEquals("YesNo", s2())
        assertEquals("Yes, No", s3())
        assertEquals("<Yes, No", s4())
        assertEquals("<Yes, No>", s5())
        assertEquals("<Yes, ...>", s6())
        assertEquals("<Yes, ~>", s7())
        assertEquals("<Yes, No>", s8())
        assertEquals("<~>", s9())


        config.locale = Locales.GERMAN
        assertEquals("", s0())
        assertEquals("Ja", s1())
        assertEquals("JaNein", s2())
        assertEquals("Ja, Nein", s3())
        assertEquals("<Ja, Nein", s4())
        assertEquals("<Ja, Nein>", s5())
        assertEquals("<Ja, ...>", s6())
        assertEquals("<Ja, ~>", s7())
        assertEquals("<Ja, Nein>", s8())
        assertEquals("<~>", s9())


    }
}