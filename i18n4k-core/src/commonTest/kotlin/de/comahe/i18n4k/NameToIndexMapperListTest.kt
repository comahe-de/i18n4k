package de.comahe.i18n4k

import de.comahe.i18n4k.messages.NameToIndexMapperList
import kotlin.test.Test
import kotlin.test.assertEquals

class NameToIndexMapperListTest {
    @Test
    fun testIndexes() {
        val m = NameToIndexMapperList("a", "ab", "c", "d")
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