package de.comahe.i18n4k.messages.formatter.types

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import de.comahe.i18n4k.messages.formatter.parsing.StylePart
import de.comahe.i18n4k.strings.LocalizedString

/**
 * Select a text value based on the named attribute value of the parameter.
 *
 * Format:
 *
 *     { PARAMETER_NUMBER, attr:NAME, VALUE1 {TEXT1} VALUE2 VALUE3 {TEXT2} /VALUE_REGEX/ {TEXT_REGEX} other {OTHERWISE_TEXT}}
 *     - NAME
 *         Name of the attribute
 *     - VALUE
 *         If a value matches the value of the attribute, the corresponding text (TEXT*) is selected
 *
 * The rest is similar to the select-pattern.
 *
 * Example:
 *
 *     {0} has forgotten {0, attr:gender, female {her} other {his} } {2, select, one {bag} other {{1} bags}}.
 *
 * Usage:
 *
 *     FORGOTTEN_BAG( PETER, 1, "one")
 *     -> Peter has forgotten his bag.
 *     FORGOTTEN_BAG( MARY, 2, "few")
 *     -> Mary has forgotten her 2 bags.
 *
 * For files storing the attribute, the name of the attribute is appended as an extension after the
 * locale tag via “-x-attr-NAME“. E.g. for the attribute “gender”:
 *
 *     names_de_DE_saxiona-x-attr-gender
 *     names_de_DE-x-attr-gender
 *     names_de-x-attr-gender
 *     names_en-x-attr-gender
 *     names_fr_FR-x-attr-gender
 *
 * e.g.
 *
 * `subjects_en.properties`
 *
 * ```properties
 * peter=male
 * mary=female
 * ```
 */
object MessageAttribSelectFormatter : MessageValueFormatter {


    override val typeId: String
        get() = "attr:"

    override val typeIdIsPrefix: Boolean
        get() = true

    override fun format(
        result: StringBuilder,
        value: Any?,
        typeId: CharSequence,
        style: StylePart?,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ) {
        if (style == null) return
        val attrName =
            if (typeId.length > this.typeId.length) typeId.subSequence(
                this.typeId.length, typeId.length
            ) else
                null

        return formatAttribute(
            result,
            attrName,
            value,
            style,
            parameters,
            locale,
            context,
        )
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun formatAttribute(
        result: StringBuilder,
        attrName: CharSequence?,
        value: Any?,
        style: StylePart?,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ) {
        val attr =
            if (attrName == null)
                null
            else
                (value as? LocalizedString)?.getAttribute(attrName, locale)

        return MessageSelectFormatter.format(
            result,
            attr,
            MessageSelectFormatter.typeId,
            style,
            parameters,
            locale,
            context,
        )
    }

}