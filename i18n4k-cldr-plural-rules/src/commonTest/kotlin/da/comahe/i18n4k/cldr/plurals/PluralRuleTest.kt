// Copyright © 2020 xyzsd
//
// See the COPYRIGHT file at the top-level directory of this
// distribution.
//
// Licensed under the Apache License, Version 2.0 <LICENSE-APACHE or
// http://www.apache.org/licenses/LICENSE-2.0> or the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT>, at your
// option. This file may not be copied, modified, or distributed
// except according to those terms.

// converted to Kotlin MP by Marcel Heckel
package da.comahe.i18n4k.cldr.plurals

import de.comahe.i18n4k.country
import de.comahe.i18n4k.forLocaleTag
import de.comahe.i18n4k.language
import de.comahe.i18n4k.cldr.plurals.PluralCategory
import de.comahe.i18n4k.cldr.plurals.PluralOperand
import de.comahe.i18n4k.cldr.plurals.PluralRule
import de.comahe.i18n4k.cldr.plurals.PluralRuleType
import de.comahe.i18n4k.cldr.plurals.PluralRules
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PluralRuleTest {
    @Test
    fun operandTests() {
        var op: PluralOperand
        op = PluralOperand.from(1)
        assertEquals(1.0, op.n)
        assertEquals(1, op.i)
        assertEquals(0, op.v)
        assertEquals(0, op.w)
        assertEquals(0, op.f)
        assertEquals(0, op.t)
        assertEquals(0, op.e)
        /* -> not stable in all platforms!
        op = PluralOperand.from(1.0)
        assertEquals(1.0, op.n)
        assertEquals(1, op.i)
        assertEquals(1, op.v)
        assertEquals(0, op.w)
        assertEquals(0, op.f)
        assertEquals(0, op.t)
        assertEquals(0, op.e)
        */
        op = PluralOperand.from("1.0")!!
        assertEquals(1.0, op.n)
        assertEquals(1, op.i)
        assertEquals(1, op.v)
        assertEquals(0, op.w)
        assertEquals(0, op.f)
        assertEquals(0, op.t)
        assertEquals(0, op.e)
        op = PluralOperand.from("1000.000")!!
        assertEquals(1000.0, op.n)
        assertEquals(1000, op.i)
        assertEquals(3, op.v)
        assertEquals(0, op.w)
        assertEquals(0, op.f)
        assertEquals(0, op.t)
        assertEquals(0, op.e)
        op = PluralOperand.from("1000.010")!!
        assertEquals(1000.01, op.n)
        assertEquals(1000, op.i)
        assertEquals(3, op.v)
        assertEquals(2, op.w)
        assertEquals(10, op.f)
        assertEquals(1, op.t)
        assertEquals(0, op.e)



        op = PluralOperand.from("234.567890", 1)!!
        assertEquals(2345.6789, op.n)
        assertEquals(2345, op.i)
        assertEquals(5, op.v)
        assertEquals(4, op.w)
        assertEquals(67890, op.f)
        assertEquals(6789, op.t)
        assertEquals(1, op.e)

        op = PluralOperand.from("234.567890", 4)!!
        assertEquals(2345678.9, op.n)
        assertEquals(2345678, op.i)
        assertEquals(2, op.v)
        assertEquals(1, op.w)
        assertEquals(90, op.f)
        assertEquals(9, op.t)
        assertEquals(4, op.e)


        op = PluralOperand.from("234.567890", 5)!!
        assertEquals(23456789.0, op.n)
        assertEquals(23456789, op.i)
        assertEquals(1, op.v)
        assertEquals(0, op.w)
        assertEquals(0, op.f)
        assertEquals(0, op.t)
        assertEquals(5, op.e)


        op = PluralOperand.from("234.567890", 6)!!
        assertEquals(234567890.0, op.n)
        assertEquals(234567890, op.i)
        assertEquals(0, op.v)
        assertEquals(0, op.w)
        assertEquals(0, op.f)
        assertEquals(0, op.t)
        assertEquals(6, op.e)

        op = PluralOperand.from(234L, 3)
        assertEquals(234000.0, op.n)
        assertEquals(234000, op.i)
        assertEquals(0, op.v)
        assertEquals(0, op.w)
        assertEquals(0, op.f)
        assertEquals(0, op.t)
        assertEquals(3, op.e)
    }

    // basic tests: cardinal
    @Test
    fun selectCardinal() {

        assertNotNull(PluralRules.selectCardinal("root", null))
        assertNotNull(PluralRules.selectCardinal("", null)) // root alias
        assertNotNull(PluralRules.selectCardinal("en", null))
        assertNotNull(PluralRules.selectCardinal("en", "invalid"))
        assertNotNull(PluralRules.selectCardinal("pt", null))
        assertNotNull(PluralRules.selectCardinal("pt", "invalid"))
        assertNotNull(PluralRules.selectCardinal("pt", "PT"))
        assertNotEquals(
            PluralRules.selectCardinal("pt", ""),
            PluralRules.selectCardinal("pt", "PT")
        )
    }

    // basic tests: ordinal
    @Test
    fun selectOrdinal() {

        assertNotNull(PluralRules.selectOrdinal("root", null))
        assertNotNull(PluralRules.selectOrdinal("", null)) // root alias
        assertNotNull(PluralRules.selectOrdinal("en", null))
        assertNotNull(PluralRules.selectOrdinal("en", "invalid"))
        assertNotNull(PluralRules.selectOrdinal("pt", null))
        assertNotNull(PluralRules.selectOrdinal("pt", "invalid"))
        assertNotNull(PluralRules.selectOrdinal("pt", "PT"))

        // for ordinals, there is no 'pt-PT'; same form as 'pt'
        assertEquals(
            PluralRules.selectOrdinal("pt", ""),
            PluralRules.selectOrdinal("pt", "PT")
        )

        // no 'ak' ordinal (CLDR 37), though there is an 'ak' cardinal; just one example of many
        assertNull(PluralRules.selectOrdinal("ak", ""))
    }

    @Test
    fun testDefaultRule() {
        val rule = PluralRule.createDefault(PluralRuleType.CARDINAL)
        assertNotNull(rule)
        assertEquals(PluralCategory.OTHER, rule.select(0))
        assertEquals(PluralCategory.OTHER, rule.select(1))
        assertEquals(PluralCategory.OTHER, rule.select(11))
        assertEquals(PluralCategory.OTHER, rule.select(15))
        assertEquals(PluralCategory.OTHER, rule.select(734823))
    }

    @Test()
    fun testCardinalSamples() {
        val cardinalSamples = readTestJSON(JsonTestData.cardinal_samples)
        println("Cardinals: " + cardinalSamples.size)
        testSamples(PluralRuleType.CARDINAL, cardinalSamples)
    }

    @Test
    fun testOrdinalSamples() {

        val ordinalSamples = readTestJSON(JsonTestData.ordinal_samples)
        println("Ordinals: " + ordinalSamples.size)
        testSamples(PluralRuleType.ORDINAL, ordinalSamples)
    }

    @Test
    fun testCompactSamples() {
        val cardinalCompactSamples = readTestJSON(JsonTestData.compact_cardinal_samples)
        println("Compact Cardinals: " + cardinalCompactSamples.size)
        testCompactSamples(PluralRuleType.CARDINAL, cardinalCompactSamples)
    }

    fun testSamples(
        type: PluralRuleType,
        allSamples: Map<String, Map<PluralCategory, List<String>>>
    ) {
        val errors = StringBuilder()
        for ((lang, value) in allSamples) {

            for ((category, samples) in value) {
                println("Testing $lang - $category")
                val locale = forLocaleTag(lang)
                // we only test languages that have rules
                val pluralRule = PluralRule.create(locale.language, locale.country, type)
                    ?: throw IllegalStateException("No rule for: $lang - ${locale.language} - ${locale.country}")

                for (sample in samples) {
                    if (pluralRule.select(sample) == null)
                        println("ääää")
                    val result: PluralCategory = pluralRule.select(sample)!!
                    if (category != result) {
                        errors.append("Failure for '$lang': '$category' sample '$sample'\n")
                    }
                }
            }
        }
        assertEquals("", errors.toString())
    }

    // compact cardinals
    fun testCompactSamples(
        type: PluralRuleType,
        allSamples: Map<String, Map<PluralCategory, List<String>>>
    ) {
        val errors = StringBuilder()
        for ((lang, value) in allSamples) {
            for ((category, samples) in value) {
                val locale = forLocaleTag(lang)
                // we only test languages that have rules
                val pluralRule = PluralRule.create(locale.language, locale.country, type)
                    ?: throw IllegalStateException("No rule for: $lang")

                for (sample in samples) {
                    val operand = parseCompact(sample)
                    val result = pluralRule.select(operand)
                    if (category != result) {
                        errors.append(
                            "Failure for '$lang': '$category' sample '$sample' (error value:'%$result','$operand')\n",
                        )
                    }
                }
            }
        }
        assertEquals("", errors.toString())
    }

    private fun parseCompact(`in`: String): PluralOperand {
        val split = `in`.indexOf('c')
        check(split > 0) { "Invalid compact form : $`in`" }
        val left = `in`.substring(0, split)
        val exp = `in`.substring(split + 1).toInt()
        return if (left.indexOf('.') >= 0) {
            PluralOperand.from(left.toDouble(), exp)
        } else {
            PluralOperand.from(left.toLong(), exp)
        }
    }


    fun readTestJSON(jsonString: String): Map<String, Map<PluralCategory, List<String>>> {

        val pluralCategoryMap = PluralCategory.entries.toTypedArray().associateBy { it.name }
        /**
         * Return the constant that matches the input String, via a case-insensitive comparison.
         * Does not throw exceptions, as valueOf() does.
         *
         * @param s case-insensitive input to match
         * @return [PluralCategory] or `null`
         */
        @Suppress("unused")
        fun parsePluralCategory(s: String): PluralCategory? {
            val pc = pluralCategoryMap[s]
            if (pc != null) {
                return pc
            } else {
                for (iter in pluralCategoryMap.values) {
                    if (iter.name.equals(s, ignoreCase = true)) {
                        return iter
                    }
                }
            }
            return null
        }

        val jsonRoot = Json.parseToJsonElement(jsonString)
        val result = mutableMapOf<String, Map<PluralCategory, List<String>>>()
        for (jsonEntry in jsonRoot.jsonObject.entries) {
            val categoryToList = mutableMapOf<PluralCategory, List<String>>()
            for (jsonSubEntry in jsonEntry.value.jsonObject.entries) {
                val category = parsePluralCategory(jsonSubEntry.key) ?: continue
                categoryToList[category] =
                    jsonSubEntry.value.jsonArray.map { it.jsonPrimitive.content }
            }
            result[jsonEntry.key] = categoryToList
        }
        return result
    }
}