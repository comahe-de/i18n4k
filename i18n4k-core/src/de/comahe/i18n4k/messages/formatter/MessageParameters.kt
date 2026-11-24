package de.comahe.i18n4k.messages.formatter

/** Retrieves the value of a named parameter of a localized string factory. */
interface MessageParameters {
    operator fun get(name: CharSequence): Any?
}