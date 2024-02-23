package da.comahe.i18n4k

import de.comahe.i18n4k.messages.NameToIndexMapperNumbersFrom1
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NameToIndexMapperNumbersFrom1Test {
    @Test
    fun testIndexes() {
        val m = NameToIndexMapperNumbersFrom1
        assertEquals(0, m.getNameIndex("1"))
        assertEquals(1, m.getNameIndex("2"))
        assertEquals(2, m.getNameIndex("3"))
        assertEquals(3, m.getNameIndex("4"))
        assertEquals(4, m.getNameIndex("5"))
        assertEquals(5, m.getNameIndex("6"))
        assertEquals(6, m.getNameIndex("7"))
        assertEquals(7, m.getNameIndex("8"))
        assertEquals(8, m.getNameIndex("9"))
        assertEquals(9, m.getNameIndex("10"))
    }


    @Test
    fun testWrongNames() {
        val m = NameToIndexMapperNumbersFrom1
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("") }
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("123") }
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("a") }
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("ab") }
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("abc") }
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("0") }
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("11") }
        assertFailsWith(IllegalArgumentException::class) { m.getNameIndex("-1") }
    }
}