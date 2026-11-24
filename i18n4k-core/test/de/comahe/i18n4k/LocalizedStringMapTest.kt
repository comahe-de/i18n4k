package de.comahe.i18n4k

import de.comahe.i18n4k.strings.LocalizedAttributeMap
import de.comahe.i18n4k.strings.LocalizedStringFactoryMap
import de.comahe.i18n4k.strings.LocalizedStringMap
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizedStringMapTest {
    @Test
    fun testToString() {

        val en = forLocaleTag("en")
        val de = forLocaleTag("de")
        val fr = forLocaleTag("fr")

        val message = LocalizedStringMap(
            en to "Hello World!",
            de to "Hallo Welt!"
        )

        assertEquals("Hello World!", message.toString(en))
        assertEquals("Hello World!", message.toString(fr))
        assertEquals("Hallo Welt!", message.toString(de))
    }

    @Test
    fun testAttributes() {
        val en = forLocaleTag("en")
        val de = forLocaleTag("de")
        val fr = forLocaleTag("fr")

        val attributesWoman = LocalizedAttributeMap(
            Triple("gender", en, "f"),
            Triple("gender", de, "f"),
            Triple("color", en, "red"),
            Triple("color", de, "rot"),
        )

        val attributesMan = LocalizedAttributeMap(
            Triple("gender", en, "m"),
            Triple("gender", de, "m"),
            Triple("color", en, "grey"),
            Triple("color", de, "grau"),
        )

        val attributesSun = LocalizedAttributeMap(
            Triple("gender", en, "n"),
            Triple("gender", de, "f"),
            Triple("color", en, "yellow"),
            Triple("color", de, "gelb"),
        )

        val attributesMoon = LocalizedAttributeMap(
            Triple("gender", en, "n"),
            Triple("gender", de, "m"),
            Triple("color", en, "white"),
            Triple("color", de, "weiß"),
        )
        val attributesWater = LocalizedAttributeMap(
            Triple("gender", en, "n"),
            Triple("gender", de, "n"),
            Triple("color", en, "blue"),
            Triple("color", de, "blau"),
        )

        val woman = LocalizedStringMap(
            en to "woman", de to "Frau",
            attributes = attributesWoman
        )
        val man = LocalizedStringMap(
            en to "man", de to "Mann",
            attributes = attributesMan,
        )
        val moon = LocalizedStringMap(
            en to "moon", de to "Mond",
            attributes = attributesMoon,
        )
        val sun = LocalizedStringMap(
            en to "sun", de to "Sonne",
            attributes = attributesSun,
        )
        val water = LocalizedStringMap(
            en to "water", de to "Wasser",
            attributes = attributesWater,
        )

        val messageThisIsX = LocalizedStringFactoryMap(
            en to "This is the {thing}. {thing, attr:gender, m {He} f {She} n {It} } has the color {thing, attr-color}.",
            de to "Das ist {thing, attr:gender, m {der} f {die} n {das} } {thing}. {thing, attr:gender, m {Er} f {Sie} n {Es} } hat die Farbe {thing, attr-color}.",
        )


        assertEquals(
            "This is the man. He has the color grey.",
            messageThisIsX("thing" to man, locale = en)
        )
        assertEquals(
            "This is the woman. She has the color red.",
            messageThisIsX("thing" to woman, locale = en)
        )
        assertEquals(
            "This is the moon. It has the color white.",
            messageThisIsX("thing" to moon, locale = en)
        )
        assertEquals(
            "This is the sun. It has the color yellow.",
            messageThisIsX("thing" to sun, locale = en)
        )
        assertEquals(
            "This is the water. It has the color blue.",
            messageThisIsX("thing" to water, locale = en)
        )

        assertEquals(
            "Das ist der Mann. Er hat die Farbe grau.",
            messageThisIsX("thing" to man, locale = de)
        )
        assertEquals(
            "Das ist die Frau. Sie hat die Farbe rot.",
            messageThisIsX("thing" to woman, locale = de)
        )
        assertEquals(
            "Das ist der Mond. Er hat die Farbe weiß.",
            messageThisIsX("thing" to moon, locale = de)
        )
        assertEquals(
            "Das ist die Sonne. Sie hat die Farbe gelb.",
            messageThisIsX("thing" to sun, locale = de)
        )
        assertEquals(
            "Das ist das Wasser. Es hat die Farbe blau.",
            messageThisIsX("thing" to water, locale = de)
        )

        // fr not defined, fallback to default locale
        assertEquals(
            "This is the man. He has the color grey.",
            messageThisIsX("thing" to man, locale = fr)
        )
    }
}