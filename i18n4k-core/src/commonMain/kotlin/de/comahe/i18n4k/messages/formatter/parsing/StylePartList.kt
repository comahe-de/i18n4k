package de.comahe.i18n4k.messages.formatter.parsing

import kotlinx.collections.immutable.ImmutableList

/** List of style modifier separated by "|" */
data class StylePartList(val list: ImmutableList<StylePart>) : StylePart {

    override val maxParameterIndex: Int
        get() = list.maxOf { it.maxParameterIndex }

}