package de.comahe.i18n4k.generator

import java.math.BigInteger
import java.text.Collator
import java.util.Locale
import kotlin.math.max

/**
 * This comparator compares strings by comparing the integer parts in them. <br></br> **Note** that
 * it only works correctly with integers without separators (e.g. 1000, but not 1,000).
 *
 * To use this class: Use the static "sort" method from the java.util.Collections class:
 * Collections.sort(your list, new AlphanumComparator());
 */
class AlphanumComparator @JvmOverloads constructor(collator: Collator? = null as Collator?) :
    Comparator<String?> {
    private lateinit var collator: Collator

    constructor(l: Locale?) : this(Collator.getInstance(l))

    init {
        var collator = collator
        if (collator == null) collator = Collator.getInstance()
        this.collator = collator!!
    }

    private fun isDigit(ch: Char): Boolean {
        return Character.isDigit(ch)
    }

    /** Length of string is passed in for improved efficiency (only need to calculate it once) */
    private fun getChunk(s: String, slength: Int, marker: Int): String {
        var marker = marker
        val chunk = StringBuilder()
        var c = s[marker]
        chunk.append(c)
        marker++
        if (isDigit(c)) {
            while (marker < slength) {
                c = s[marker]
                if (!isDigit(c)) break
                chunk.append(c)
                marker++
            }
        } else {
            while (marker < slength) {
                c = s[marker]
                if (isDigit(c)) break
                chunk.append(c)
                marker++
            }
        }
        return chunk.toString()
    }

    override fun compare(o1: String?, o2: String?): Int {
        if (o1 == null) {
            return if (o2 == null) 0 else 1
        }
        if (o2 == null) return -1
        val s1: String = o1
        val s2: String = o2
        var thisMarker = 0
        var thatMarker = 0
        val s1Length = s1.length
        val s2Length = s2.length
        while (thisMarker < s1Length && thatMarker < s2Length) {
            val thisChunk = getChunk(s1, s1Length, thisMarker)
            thisMarker += thisChunk.length
            val thatChunk = getChunk(s2, s2Length, thatMarker)
            thatMarker += thatChunk.length

            // If both chunks contain numeric characters, sort them numerically
            var result = 0
            result = if (isDigit(thisChunk[0]) && isDigit(thatChunk[0])) {
                // too long for long
                if (max(thisChunk.length.toDouble(), thatChunk.length.toDouble()) > 18) {
                    BigInteger(thisChunk).compareTo(
                        BigInteger(
                            thatChunk
                        )
                    )
                } else {
                    java.lang.Long.compare(thisChunk.toLong(), thatChunk.toLong())
                }
            } else {
                collator.compare(thisChunk, thatChunk)
            }
            if (result != 0) return result
        }
        return s1Length - s2Length
    }

    companion object {
        val INSTANCE_ENGLISH = AlphanumComparator(Collator.getInstance(java.util.Locale.ENGLISH))
    }
}
