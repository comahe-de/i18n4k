package de.comahe.i18n4k

import de.comahe.i18n4k.strings.LocalizedString

typealias StringOrLocalizedString = Any

class LocalizedText(vararg strings: StringOrLocalizedString) : LocalizedString {

    var separator = ""
    var prefix = ""
    var postfix = ""
    var limit = -1
    var truncated = "..."
    var transform: (LocalizedString) -> CharSequence = { it() }
    val list = strings.map {
        if (it is String) it.asLocalizedString
        else it as LocalizedString
    }.toMutableList()

    fun append(vararg other: StringOrLocalizedString) {
        for (str in other)
            if (str is String)
                plusAssign(str)
            else
                plusAssign(str as LocalizedString)
    }

    operator fun plusAssign(other: LocalizedString) {
        list += other
    }

    operator fun plusAssign(other: String) {
        list += other.asLocalizedString
    }

    override operator fun invoke() = toString()
    override fun toString(): String = toString(null)
    override fun toString(locale: Locale?): String =
        list.joinToString(separator, prefix, postfix, limit, truncated) { it(locale) }

    fun joinToString(
        separator: String = this.separator,
        prefix: String = this.prefix,
        postfix: String = this.postfix,
        limit: Int = this.limit,
        truncated: String = this.truncated,
        transform: (LocalizedString) -> CharSequence = this.transform
                    ): String = list.joinToString(separator, prefix, postfix, limit, truncated, transform)

    val isEmpty: Boolean
        get() = list.isEmpty() || list.all { it().isEmpty() }

    val isNotEmpty: Boolean
        get() = !isEmpty
}