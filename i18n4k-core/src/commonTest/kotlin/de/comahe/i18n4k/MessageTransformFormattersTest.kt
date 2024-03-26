package de.comahe.i18n4k

import de.comahe.i18n4k.config.I18n4kConfigDefault
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault.format
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageTransformFormattersTest {

    private var i18n4kConfig = I18n4kConfigDefault()

    @BeforeTest
    fun init() {
        i18n4k = i18n4kConfig
        i18n4kConfig.restoreDefaultSettings()
    }

    /**
     * Test formats of
     * [de.comahe.i18n4k.messages.formatter.types.MessageTransformFormatters]
     */
    @Test
    fun test_transform_single() {
        val locale = Locale("en")
        val params = listOf("hello WORLD")

        assertEquals("HELLO WORLD!", format("{0, uppercase }!", params, locale))
        assertEquals("hello world!", format("{0, lowercase }!", params, locale))
        assertEquals("Hello WORLD!", format("{0, capitalize }!", params, locale))
    }

        /**
     * Test formats of
     * [de.comahe.i18n4k.messages.formatter.types.MessageTransformFormatters]
     */
    @Test
    fun test_transform_textMessage() {
        val locale = Locale("en")
        val params = listOf("hello WORLD", "oK")

        assertEquals("HELLO WORLD-OK!", format("{~, uppercase, {{0}-{1}} }!", params, locale))
        assertEquals("hello world-ok!", format("{~, lowercase, {{0}-{1}} }!", params, locale))
        assertEquals("Hello WORLD-oK!", format("{~, capitalize, {{0}-{1}} }!", params, locale))

        assertEquals("@hello WORLD-oK!", format("{~, capitalize, {@{0}-{1}} }!", params, locale))
    }

     @Test
    fun test_invalid() {
        val locale = Locale("en")
        val params = listOf("hello WORLD")

        assertEquals("!", format("{~, uppercase }!", params, locale))
        assertEquals("{1}!", format("{~, lowercase, {{1}} }!", params, locale))
        assertEquals("{1}!", format("{~, capitalize, {{1}} {{0}} }!", params, locale))
     }
}