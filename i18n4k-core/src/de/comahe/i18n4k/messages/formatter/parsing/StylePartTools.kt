package de.comahe.i18n4k.messages.formatter.parsing

/** Returns the first message part of the style */
fun StylePart.firstMessagePart(): MessagePart? {
    return when (this) {
        is StylePartMessage -> this.messagePart
        is StylePartArgument -> null
        is StylePartList -> this.list.firstNotNullOf { it.firstMessagePart() }
    }
}

fun StylePart.firstArgument(): CharSequence? {
    return when (this) {
        is StylePartMessage -> null
        is StylePartArgument -> this.value
        is StylePartList -> this.list.firstNotNullOf { it.firstArgument() }
    }
}
