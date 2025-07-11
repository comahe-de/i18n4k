package de.comahe.i18n4k.messages.formatter.parsing

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** List of style modifier separated by "|" */
data class StylePartList(val list: ImmutableList<StylePart>) : StylePart {

    constructor(part: StylePart) : this(persistentListOf(part))
    constructor(vararg parts: StylePart) : this(persistentListOf(*parts))

    override fun fillInParameterNames(names: MutableList<Pair<CharSequence, CharSequence?>>) {
        for (part in list)
            part.fillInParameterNames(names)
    }

}