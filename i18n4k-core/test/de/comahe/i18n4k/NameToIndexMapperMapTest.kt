package de.comahe.i18n4k

import de.comahe.i18n4k.messages.NameToIndexMapperMap
import kotlin.test.Test
import kotlin.test.assertEquals

class NameToIndexMapperMapTest {
    @Test
    fun testIndexes() {
        val m = NameToIndexMapperMap("a" to 0, "ab" to 1, "c" to 2, "d" to 3)
        assertEquals(0, m.getNameIndex("a"))
        assertEquals(1, m.getNameIndex("ab"))
        assertEquals(2, m.getNameIndex("c"))
        assertEquals(3, m.getNameIndex("d"))


        assertEquals(-1, m.getNameIndex(""))
        assertEquals(-1, m.getNameIndex("0"))
        assertEquals(-1, m.getNameIndex("e"))
        assertEquals(-1, m.getNameIndex("A"))
        assertEquals(-1, m.getNameIndex("AB"))
    }
}