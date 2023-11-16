package de.comahe.i18n4k.messages.formatter.parsing

import kotlinx.collections.immutable.ImmutableSet

/** Style part that has name(s). Data is separated by ":" */
data class StylePartNamed(val names: ImmutableSet<CharSequence>, val data: MessagePart) :
    StylePart {

    override val maxParameterIndex: Int
        get() = data.maxParameterIndex
}
