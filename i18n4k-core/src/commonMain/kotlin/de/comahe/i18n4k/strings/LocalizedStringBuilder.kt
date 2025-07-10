package de.comahe.i18n4k.strings

import de.comahe.i18n4k.Locale
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

/**
 * A builder class for creating a composite `LocalizedString` by combining multiple parameter values
 * with customizable formatting options.
 *
 * Parameter values are checked, if they are `LocalizedString` and the locale is applied when
 * creating a new string.
 *
 * The [Any.toString] or [LocalizedString.toString] method of the parameters is evaluated on each
 * call of [toString] or [build]
 *
 * This class implements the `LocalizedString` interface, allowing for locale-dependent string
 * representations.
 *
 * Strings or localized strings are appended to a mutable list and can be joined into a single
 * string representation with configurable separators, prefixes, postfixes, truncation, limits, and
 * transformation functions.
 *
 * @param parameters The initial strings or localized strings to include in the builder.
 * @constructor Creates a `LocalizedStringBuilder` instance
 */

class LocalizedStringBuilder() : LocalizedString {

    /** Used separator between the parameters. Default "". */
    var separator: LocalizedString? = null

    /** Used prefix of the generated string. Default "". */
    var prefix: LocalizedString? = null

    /** Used postfix of the generated string. Default "". */
    var postfix: LocalizedString? = null

    /**
     * If the collection could be huge, you can specify a non-negative value of limit, in which
     * case only the first limit elements will be appended, followed by the truncated string (which
     * defaults to "...").
     */
    var limit = -1

    /** String appended when to many elements are added. */
    var truncated: LocalizedString = SimpleLocalizedString("...")

    /** Transforms objects before creating the string */
    var transform: (Any) -> Any? = { it }

    /** List of parameter object */
    private val parameters: MutableList<Any> = mutableListOf()


    constructor(value: CharSequence) : this() {
        append(value)
    }

    constructor(value: LocalizedString) : this() {
        append(value)
    }

    fun append(vararg other: CharSequence): LocalizedStringBuilder {
        parameters.addAll(other.toList())
        return this
    }

    operator fun plusAssign(other: CharSequence) {
        parameters.add(other)
    }

    fun append(vararg other: LocalizedString): LocalizedStringBuilder {
        parameters.addAll(other.toList())
        return this
    }

    operator fun plusAssign(other: LocalizedString) {
        parameters.add(other)
    }

    override fun toString(): String = toString(null)

    override fun toString(locale: Locale?): String =
        parameters.joinToString(
            separator?.toString(locale) ?: "",
            prefix?.toString(locale) ?: "",
            postfix?.toString(locale) ?: "",
            limit,
            truncated.toString(locale)
        ) { transform(it).toString(locale) }

    /**
     * Build the new [LocalizedString] with the current parameters.
     *
     * Later changes on this builder will not change the returned [LocalizedString]
     */
    fun build(): LocalizedString {
        val list = ArrayList<Any>(parameters.size * 2 + 3)
        prefix?.let { list.add(it) }
        var count = 0
        for (parameter in parameters) {
            if (++count > 1)
                separator?.let { list.add(it) }
            if (limit < 0 || count <= limit)
                transform(parameter)?.let {
                    list.add(
                        when (it) {
                            is LocalizedString -> it
                            is CharSequence -> it
                            else -> it.toString()
                        }
                    )
                }
            else
                break
        }
        @Suppress("ConvertTwoComparisonsToRangeCheck")
        if (limit >= 0 && count > limit)
            list.add(truncated)

        postfix?.let { list.add(it) }
        return LocalizedStringBuild(list.toPersistentList())
    }

    private class LocalizedStringBuild(
        private val parameters: PersistentList<Any>
    ) : LocalizedString {
        override fun toString(): String = toString(null)

        override fun toString(locale: Locale?): String {
            return parameters.joinToString(separator = "") { it.toCharSequence(locale) }
        }
    }
}

