package de.comahe.i18n4k.strings

/**
 * Returns a copy (if needed) of this string having its first letter titlecased using the rules of
 * the default locale, or the original string if it's empty or already starts with a title case
 * letter.
 *
 * This function does not support one-to-many character mapping. If needed, see [capitalizeFull].
 *
 * Implemented because the Kotlin-Std-Lib capitalize is duplicated
 */
fun String.capitalize(): String {
    if (isEmpty())
        return this
    if (!this[0].isLowerCase())
        return this
    return replaceFirstChar { it.titlecaseChar() }
}

/**
 * Returns a copy (if needed) of this string having its first letters are titlecased using the rules
 * of the default locale, or the original string if it's empty or already starts with a title case
 * letter.
 *
 * This function supports one-to-many character mapping, thus the length of the returned string
 * can be greater than one. For example, `'\uFB00'.titlecase()` returns `"\u0046\u0066"`, where
 * `'\uFB00'` is the LATIN SMALL LIGATURE FF character (`ﬀ`). If this character has no title case
 * mapping, the result of [uppercase] is returned instead.
 *
 * @see Char.titlecase
 */
fun String.capitalizeFull(): String {
    if (isEmpty())
        return this
    if (!this[0].isLowerCase())
        return this
    return replaceFirstChar { it.titlecase() }
}