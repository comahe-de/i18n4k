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
@file:Suppress("DuplicatedCode")

package de.comahe.i18n4k.cldr.plurals

import de.comahe.i18n4k.*
import de.comahe.i18n4k.cldr.plurals.PluralOperand.Companion.from


/**
 * Wrapper class for PluralRules, designed for ease-of-use and safety.
 *
 * A PluralRule is created by using a `create()` method. Create methods will return empty Optionals
 * if a no plural rule is found for a given language. Optional method chaining can be used to return
 * a default rule, or `createOrDefault()` can be used (less error-prone).
 *
 * For example:
 * ```
 * PluralRule rule = PluralRule.createOrDefault(Locale.ENGLISH, PluralRuleType.ORDINAL);
 * assert (rule.select(1) == PluralCategory.ONE);                  // e.g., "1 day"
 * assert (rule.select(10) == PluralCategory.OTHER);               // e.g., "10 days"
 * assert (rule.select("1100.00") == PluralCategory.OTHER);        // e.g., "1100.00 days"
 * <p>
 * PluralRule rule = PluralRule.createOrDefault(Locale.ENGLISH, PluralRuleType.CARDINAL);
 * assert (rule.select(1) == PluralCategory.ONE);              // e.g, "1st"   use 'st' suffix
 * assert (rule.select(2) == PluralCategory.TWO);              // e.g., "2nd"  use 'nd' suffix
 * assert (rule.select(3) == PluralCategory.FEW);              // e.g., "3rd"  use 'rd' suffix
 * assert (rule.select(4) == PluralCategory.OTHER);            // e.g., "4th"  use 'th' suffix
 * assert (rule.select(43) == PluralCategory.FEW);             // e.g., "43rd"
 * assert (rule.select(50) == PluralCategory.OTHER);           // e.g., "50th"
 * ```
 *
 * The PluralCategory returned determines how subsequent localization logic then handles the number,
 * ranking, or quantity, which is locale-dependent.
 *
 * When matching a language, the empty String "" and String "root" are equivalent to Locale.ROOT.
 * The Locale.ROOT rule can be used if there is no match for a language. This is particularly
 * important when using cardinal rules; many languages do not have specific cardinal rules.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class PluralRule private constructor(
    private val type: PluralRuleType,
    private val rule: ((PluralOperand) -> PluralCategory),
    val lang: String,
    val region: String
) {

    /**
     * Returns the PluralRuleType corresponding to this PluralRule. Never null.
     *
     * @return PluralRuleType corresponding to this PluralRule.
     */
    fun pluralRuleType(): PluralRuleType {
        return type
    }

    /**
     * Returns the rule as a Function. Never null.
     *
     * @return PluralRule (as a Function)
     */
    fun rule(): ((PluralOperand) -> PluralCategory) {
        return rule
    }

    /**
     * Determine the PluralCategory for the given PluralOperand.
     *
     * Base convenience method, equivalent to `rule().apply(op)`. Null values are not allowed.
     *
     * @param op PluralOperand
     * @return PluralCategory for the given value.
     */
    fun select(op: PluralOperand?): PluralCategory? {
        if (op == null) return null
        return rule(op)
    }

    /**
     * Determine the PluralCategory for the given numeric String.
     *
     * Using String or BigDecimal PluralOperands permits the retention of precision (trailing
     * zeros), which can affect localization.
     *
     * This will return an empty Optional if the String cannot be successfully parsed.
     *
     * @param value Numeric value, as a String
     * @return Optional containing the PluralCategory. Empty if String parsing fails.
     * @throws NullPointerException if value is null
     */
    fun select(value: String): PluralCategory? {
        return select(from(value))
    }

    /**
     * Determine the PluralCategory for the given Number.
     *
     * Handled as per PluralOperand.from(Number). Using BigDecimal permits the retention of
     * precision (trailing zeros), unlike doubles or floats, which can affect localization.
     *
     * @param value value as a Number
     * @return PluralCategory for the given value.
     * @throws NullPointerException if value is null
     */
    fun select(value: Number): PluralCategory? {
        return select(from(value))
    }


    /**
     * Determine the PluralCategory for the given "compact" long
     *
     * @param value input value
     * @param suppressedExponent suppressed exponent (range: 0-21)
     * @return PluralOperand
     */
    fun selectCompact(value: Long, suppressedExponent: Int): PluralCategory? {
        return select(from(value, suppressedExponent))
    }

    /**
     * Determine the PluralCategory for the given "compact" double
     *
     * @param value input value
     * @param suppressedExponent suppressed exponent (range: 0-21)
     * @return PluralOperand
     */
    fun selectCompact(value: Double, suppressedExponent: Int): PluralCategory? {
        return select(from(value, suppressedExponent))
    }

    /**
     * Determine the PluralCategory for the given `double` value.
     *
     * Non-finite values will return `PluralCategory.OTHER`.
     *
     * @param value value as a `double`
     * @return PluralCategory for the given value.
     */
    fun select(value: Double): PluralCategory? {
        return if (value.isFinite())
            select(from(value))
        else
            PluralCategory.OTHER
    }

    /**
     * Determine the PluralCategory for the given `long` value.
     *
     * @param value value as a `long`
     * @return PluralCategory for the given value.
     */
    fun select(value: Long): PluralCategory? {
        return select(from(value))
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PluralRule) return false

        if (type != other.type) return false
        if (rule != other.rule) return false
        if (lang != other.lang) return false
        if (region != other.region) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (rule.hashCode())
        result = 31 * result + (lang.hashCode())
        result = 31 * result + region.hashCode()
        return result
    }

    override fun toString(): String {
        return "PluralRule{" +
            "type=" + type +
            ", rule=" + rule +
            ", lang='" + lang + '\'' +
            ", region='" + region + '\'' +
            '}'
    }

    companion object {
        /** Locale.ROOT (default rule) for ordinals */
        val ROOT_ORDINAL = createRoot(PluralRuleType.ORDINAL)

        /** Locale.ROOT (default rule) for cardinals */
        val ROOT_CARDINAL = createRoot(PluralRuleType.CARDINAL)

        /**
         * The Locale for this plural rule.
         *
         * This may not be the same Locale as supplied to the [PluralRule.create] supplied Locale.
         * For example, both "en-US" and "en-GB" will return a Locale with only the language "en"
         * specified, since the region (in the case of English) is not relevant to plural selection.
         *
         * @param pr PluralRule for which we should determine the Locale. Null not allowed.
         * @return Locale for this Plural rule. Never null.
         */
        fun locale(pr: PluralRule): Locale {
            return if (pr === ROOT_ORDINAL || pr === ROOT_CARDINAL || "" == pr.lang) {
                rootLocale
            } else {
                createLocale(language = pr.lang, country = pr.region)
            }
        }

        /**
         * Create the PluralRule for a given language and (optionally) region.
         *
         * Note that while the language must match, the region is optional. Unspecified or invalid
         * regions will match the language type. Empty String values are permitted; null values are
         * not.
         *
         * @param language BCP 47 language code (lower case), "root", or empty String ""
         * @param region two-letter ISO 3166 region **normalized to upper case**, or empty String ""
         * @param type PluralRuleType (ORDINAL or CARDINAL)
         * @return PluralRule for given language and PluralRuleType. null if unmatched language or
         *     no PluralType for the given language.
         */

        fun create(language: String, region: String, type: PluralRuleType): PluralRule? {
            val role = ruleByType(language, region, type) ?: return null
            return PluralRule(type, role, language, region)
        }

        /**
         * Create the PluralRule for a given Locale.
         *
         * @param locale Locale to match language and (possibly) country. Null not permitted.
         * @param type PluralRuleType (ORDINAL or CARDINAL). Null not permitted.
         * @return PluralRule for given language and PluralRuleType. null if unmatched language or
         *     no PluralType for the given language.
         */

        fun create(locale: Locale, type: PluralRuleType): PluralRule? {
            return create(locale.language, locale.country, type)
        }

        /**
         * Create the PluralRule for a given language and (optionally) region.
         *
         * If the rule cannot be created, or no rule exists, the default (Locale.ROOT) rule is
         * returned.
         *
         * @param locale Locale to match language and (possibly) country
         * @param type PluralRuleType (ORDINAL or CARDINAL)
         * @return PluralRule for matching language and PluralRuleType, or the Locale.ROOT rule if
         *     unmatched
         */


        fun createOrDefault(locale: Locale, type: PluralRuleType): PluralRule {
            return create(locale, type) ?: createDefault(type)
        }

        /**
         * Create the PluralRule for a given language and (optionally) region.
         *
         * If the rule cannot be created, or no rule exists, the default (Locale.ROOT) rule is
         * returned.
         *
         * @param language BCP 47 language code (lower case), "root", or empty String "" Null is not
         *     permitted.
         * @param region two-letter ISO 3166 region **normalized to upper case**, or empty String
         *     "". Null is not permitted
         * @param type PluralRuleType (ORDINAL or CARDINAL). Null is not permitted.
         * @return PluralRule for given language and PluralRuleType. Empty if unmatched language or
         *     no PluralType for the given language.
         */


        fun createOrDefault(language: String, region: String, type: PluralRuleType): PluralRule {
            return create(language, region, type) ?: createDefault(type)
        }

        /**
         * The 'default' PluralRule, equivalent to the rule for Locale.ROOT
         *
         * The default PluralRule always returns `PluralCategory.OTHER` for
         * `select()`. This can be used to guarantee a result: `PluralRule myRule =
         * create(Locale.EXAMPLE, PluralRule.ORDINAL).orElse(createDefault(PluralRule.ORDINAL)`
         *
         * @param type PluralRuleType
         * @return default PluralRule
         */

        fun createDefault(type: PluralRuleType): PluralRule {
            return when (type) {
                PluralRuleType.CARDINAL -> ROOT_CARDINAL
                PluralRuleType.ORDINAL -> ROOT_ORDINAL
            }
        }

        private fun ruleByType(
            language: String,
            region: String,
            type: PluralRuleType
        ): ((PluralOperand) -> PluralCategory)? {
            return when (type) {
                PluralRuleType.CARDINAL -> PluralRules.selectCardinal(language, region)
                PluralRuleType.ORDINAL -> PluralRules.selectOrdinal(language, region)
            }
        }

        // create Locale.ROOT 'default' rule
        private fun createRoot(type: PluralRuleType): PluralRule {
            return PluralRule(
                type,
                { PluralCategory.OTHER },
                "",
                ""
            )
        }
    }
}
