package de.comahe.i18n4k.messages.formatter.parsing

/** Style modifier */
sealed interface StylePart {

    /**
     * returns the max used parameter index in the message. "-1" if there is no
     * parameter
     */
    val maxParameterIndex: Int
}
