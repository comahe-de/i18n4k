package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.cldr.plurals.PluralCategory
import de.comahe.i18n4k.cldr.plurals.PluralRule
import de.comahe.i18n4k.messages.formatter.parsing.StylePart
import de.comahe.i18n4k.messages.formatter.types.MessageSelectFormatter

/**
 * It is like a normal select-pattern (#36), but instead of using the number value of the parameter
 * directly, the number is passed to a plural-provider that returns the plural typed of the number.
 *
 * Predefined return values of the gender-provider zero one two few many other
 *
 * The plural-provider may also return other values, for currently unconsidered cases.
 *
 * The are two different plural rule types
 * [cardinals](https://en.wikipedia.org/wiki/Cardinal_numeral)
 * [ordinals](https://en.wikipedia.org/wiki/Ordinal_numeral)
 *
 * The default plural provider be based on the [Plural Rules](
 * https://cldr.unicode.org/index/cldr-spec/plural-rules) of the Unicode CLDR. They have a large
 * collection of [rules](
 * https://www.unicode.org/cldr/charts/43/supplemental/language_plural_rules.html) for almost all
 * language. They also provide JSON files in a special [format](
 * http://unicode.org/reports/tr35/tr35-numbers.html#Language_Plural_Rules) for [cardinals](
 * https://raw.githubusercontent.com/unicode-org/cldr-json/master/cldr-json/cldr-core/supplemental/plurals.json)
 * and
 * [ordinals](https://raw.githubusercontent.com/unicode-org/cldr-json/master/cldr-json/cldr-core/supplemental/ordinals.json).
 *
 * For cardinals the “plural” key is used in the pattern. For ordinals the “ordinal” key is used.
 *
 * Example “plural”:
 * ```
 * COUNT_BAGS =  There {0, plural, one: is | ? are } {0} {0, plural, one: bag | ? bags}.
 *
 * COUNT_BAGS(1) -> There is 1 bag.
 * COUNT_BAGS(2) -> There are 2 bags.
 * ```
 *
 * Example “ordinal”:
 * ```
 * METING_NUMBER  = This is the {0}{0, ordinal, one: st | two: nd  | few: rd | ? th } meeting.
 *
 * METING_NUMBER(1) -> This is the 1st meeting.
 * METING_NUMBER(4) -> This is the 4th meeting.
 * ```
 */
abstract class MessagePluralFormatter : MessageValueFormatter {

    override fun format(
        result: StringBuilder,
        value: Any?,
        typeId: CharSequence,
        style: StylePart?,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext,
    ) {
        val role = getPluralRule(locale)

        val category: PluralCategory? =
            if (role == null)
                null
            else
                when (value) {
                    null -> null
                    is String -> role.select(value)
                    is Number -> role.select(value)
                    else -> role.select(value.toString())
                }

        return MessageSelectFormatter.format(
            result,
            category?.id,
            MessageSelectFormatter.typeId,
            style,
            parameters,
            locale,
            context,
        )
    }

    protected abstract fun getPluralRule(
        locale: Locale,
    ): PluralRule?
}