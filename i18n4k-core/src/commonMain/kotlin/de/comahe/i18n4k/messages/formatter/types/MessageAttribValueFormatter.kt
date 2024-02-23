package de.comahe.i18n4k.messages.formatter.types

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageParameters
import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import de.comahe.i18n4k.messages.formatter.parsing.StylePart
import de.comahe.i18n4k.messages.formatter.parsing.firstMessagePart
import de.comahe.i18n4k.strings.LocalizedString

/**
 * Inserts the values of the given attribute of the parameter
 *
 * Format:
 *
 *     { PARAMETER_NUMBER, attr-NAME, {DEFAULT}}
 *     - NAME
 *         Name of the attribute
 *     - DEFAULT
 *         Optional default value, if the attribute is null
 *
 * Example:
 *
 *     {0} has gender {0, attr-gender, {unknown}}.
 *
 * Usage:
 *
 *     FORGOTTEN_BAG( PETER )
 *     -> Peter has gender male.
 *     FORGOTTEN_BAG( MARY )
 *     -> Mary has gender female.
 *
 * For files storing the attribute, the name of the attribute is appended as an extension after the
 * locale tag via “-x-attr-NAME“. E.g. for the attribute “gender”:
 *
 *     names_de_DE_saxiona-x-attrib-gender
 *     names_de_DE-x-attrib-gender
 *     names_de-x-attrib-gender
 *     names_en-x-attrib-gender
 *     names_fr_FR-x-attrib-gender
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
object MessageAttribValueFormatter : MessageValueFormatter {


    override val typeId: String
        get() = "attr-"

    override val typeIdIsPrefix: Boolean
        get() = true

    override fun format(
        result: StringBuilder,
        value: Any?,
        typeId: CharSequence,
        style: StylePart?,
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    ) {
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
        parameters: MessageParameters,
        locale: Locale,
        context: MessageFormatContext
    ) {
        val attr =
            if (attrName == null)
                null
            else
                (value as? LocalizedString)?.getAttribute(attrName, locale)

        if (attr == null)
            style?.firstMessagePart()?.format(result, parameters, locale, context)
                ?: result.append(value)
        else
            result.append(attr)
    }

}