package de.comahe.i18n4k.messages.formatter.types

import de.comahe.i18n4k.i18n4k
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageParameters
import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import de.comahe.i18n4k.messages.formatter.parsing.StylePart
import de.comahe.i18n4k.messages.formatter.parsing.firstMessagePart
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProvider
import de.comahe.i18n4k.messages.formatter.provider.DeclensionProviderDefault
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

/**
 * For languages that have irregular declension, a declension-provider is needed to find the
 * declined form of a word.
 *
 * Format:
 *
 * `{ PARAMETER_NUMBER, decl-DECLENSION_FORM, {DEFAULT_VALUE} }`
 * * `PARAMETER_NUMBER`
 *    * Number of the parameter which value is matched against the values of the select list
 * * `DECLENSION_FORM`
 *    * Name of the declension form. The value of the parameter should be transformed to this
 *      declension form.
 * * DEFAULT_VALUE
 *       * Optional default value, if the attribute is null
 *
 * A general declension-provider may be based on large directories. But this is an extensive task.
 * Therefor the default implementation will use the attributes of the `LocalizedString` (#43).
 *
 * Example:
 * * `HIGH_RISK_OF`
 *    * en: `The risk of a {0} is high.`
 *    * de: `Das Risiko eines {0, declension-genitive, {{0}s} } ist groß.`
 * * HIGH_RISK_OF( STORM )
 *    * en: `The risk of a storm is high.`
 *    * de: `Das Risiko eines Sturms ist groß.`
 * * HIGH_RISK_OF( SPARK )
 *    * en: `The risk of a spark is high.`
 *    * de: `Das Risiko eines Funkens ist groß.`
 *
 * These subjects-strings should be defined in a separate `MessageBundle` where also the
 * declension-form attribute is defined. The value of "DECLENSION_FORM" is the name of the attribute
 * that is looked up.
 *
 * For files storing the attribute, the name of the attribute is appended as
 * [extension](https://www.w3.org/International/articles/language-tags/#extension)
 * after the locale tag via “-x-attr-declension-NAME“
 *
 * `subjects_en.properties`
 *
 * ```
 * spark=spark
 * storm=storm
 * ```
 *
 * No declension for English.
 *
 * `subjects_de.properties`
 *
 * ```
 * spark=Funke
 * storm=Sturm
 * ```
 *
 * `subjects_de_-x-attr-decl-genitive.properties`
 *
 * ```
 * spark=Funkens
 * ```
 *
 * (“Sturm” has regular declension and doesn’t need a declension entry.)
 */
object MessageDeclensionValueFormatter : MessageValueFormatter {

    override val typeId: String
        get() = "decl-"

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
        val declensionCase =
            if (typeId.length > this.typeId.length) typeId.subSequence(
                this.typeId.length, typeId.length
            ) else
                null

        val declensionValue = declensionCase?.let {
            i18n4k.declensionProvider.getDeclensionOf(it, value, locale)
        }

        if (declensionValue == null)
            style?.firstMessagePart()?.format(result, parameters, locale, context)
                ?: result.append(value)
        else
            result.append(declensionValue)
    }
}