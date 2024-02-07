package de.comahe.i18n4k.messages.formatter.parsing

interface TextWithParameters {

    /**
     * returns the max used parameter index in the message. "-1" if there is no parameter or at
     * least on named parameter.
     */
    val maxParameterIndex: Int

    /** True if the message text contains named parameters. */
    val hasNamedParameters: Boolean
}