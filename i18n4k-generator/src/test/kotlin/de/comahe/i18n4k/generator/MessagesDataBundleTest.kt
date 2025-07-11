package de.comahe.i18n4k.generator

import de.comahe.i18n4k.Locales
import de.comahe.i18n4k.messages.formatter.MessageFormatterDefault
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MessagesDataBundleTest {

    /**
     * Test that inconsistent value type declarations of different [MessagesData] lead to an
     * exception.
     */
    @Test
    fun testInconsistentValueTypes() {
        val bundle = createBundle()
        bundle.addMessagesData(
            MessagesData(
                Locales.ENGLISH,
                mapOf(
                    "a" to "{0:Int}{1:String}{2}",
                    "b" to "{0:Int}{1:Byte}",
                    "c" to "{0:Int}{0:Byte}"
                )
            )
        )
        bundle.addMessagesData(
            MessagesData(
                Locales.GERMAN,
                mapOf(
                    "a" to "{0:Int}{1:Long}{2}",
                    "b" to "{0:Int}{1:Byte}",
                    "c" to "{0:Int}{0:Byte}"
                )
            )
        )

        assertEquals(mapOf("0" to "Int", "1" to "Byte"), bundle.getMessageParametersNames("b"))

        // different type in different bungles
        assertEquals(
            "Parameters have declared different value types" +
            " - Bundle: pack.test-bundle - Key: a - Parameter: 1" +
            " - Value type 1: String" +
            " - Value type 2: Long",
            assertThrows(IllegalStateException::class.java) {
                bundle.getMessageParametersNames("a")
            }.message
        )

        // different type in the same message
        assertEquals(
            "Parameters have declared different value types" +
                " - Bundle: pack.test-bundle - Key: c - Parameter: 0" +
                " - Value type 1: Int" +
                " - Value type 2: Byte",
            assertThrows(IllegalStateException::class.java) {
                bundle.getMessageParametersNames("c")
            }.message
        )

    }


    private fun createBundle(): MessagesDataBundle {
        return MessagesDataBundle(BundleName("pack", "test-bundle"), MessageFormatterDefault)
    }
}