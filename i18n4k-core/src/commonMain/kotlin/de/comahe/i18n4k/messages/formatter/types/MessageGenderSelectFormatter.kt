package de.comahe.i18n4k.messages.formatter.types

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.formatter.MessageFormatContext
import de.comahe.i18n4k.messages.formatter.MessageValueFormatter
import de.comahe.i18n4k.messages.formatter.parsing.StylePart
import de.comahe.i18n4k.messages.formatter.provider.GenderProvider
import de.comahe.i18n4k.messages.formatter.provider.GenderProviderDefault
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

/**
 * It is like a normal select-pattern (#36), but instead of using the value of the parameter
 * directly, the value is passed to a gender-provider that returns the gender of the parameter
 * value.
 *
 * Predefined return values of the gender-provider
 * * m: masculine
 * * f: feminine
 * * n: neuter
 * * c: common
 * * u: unknown/undefined
 *
 * The gender-provider may also return other values for currently unconsidered cases.
 *
 * Example:
 * ```
 * FORGOTTER_BAG = The {0} has forgotten {0, gender, m: his | f: her | ? its) bag.
 *
 * FORGOTTER_BAG("boy")  ->  The boy has forgotten his bag.
 * ```
 *
 * A general purpose gender-provider may be based on large directories. But this is an extensive
 * task. Therefor the default implementation will use the attributes of the `LocalizedString`
 * (#43), like the “attrib-select-pattern”. So the example above would be written as
 *
 * `FORGOTTER_BAG( BOY )`
 *
 * These subjects-string should be defined in a separate MessageBundle where also the “gender”
 * attribute is defined, e.g.
 *
 * `subjects_en.properties`
 *
 * ```properties
 * boy=boy
 * girl=girl
 * child=child
 * ```
 *
 * `subjects_en_-x-attr-gender.properties`
 *
 * ```properties
 * boy=m
 * girl=f
 * child=n
 * ```
 */
object MessageGenderSelectFormatter : MessageValueFormatter {

    private val genderProviderRef = atomic<GenderProvider>(GenderProviderDefault)

    @Suppress("MemberVisibilityCanBePrivate")
    var genderProvider: GenderProvider
        get() = genderProviderRef.value
        set(value) = genderProviderRef.update { value }

    override val typeId: String
        get() = "gender"

    override fun format(
        result: StringBuilder,
        value: Any?,
        typeId: CharSequence,
        style: StylePart?,
        parameters: List<Any>,
        locale: Locale,
        context: MessageFormatContext
    ) {
        return MessageSelectFormatter.format(
            result,
            genderProvider.getGenderOf(value, locale),
            MessageSelectFormatter.typeId,
            style,
            parameters,
            locale,
            context,
        )
    }
}