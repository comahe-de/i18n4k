package de.comahe.i18n4k.messages.formatter.parsing

/** Searches the first [StylePartSimple], if not found takes the fist named. */
fun StylePart.toSimple(): MessagePart? {
    return when (this) {
        is StylePartSimple -> this.data
        is StylePartNamed -> this.data
        is StylePartList -> this.list.firstNotNullOfOrNull { it as? StylePartSimple }?.data
            ?: this.list.firstNotNullOfOrNull { it as? StylePartNamed }?.data

    }
}