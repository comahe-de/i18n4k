package de.comahe.i18n4k.messages.formatter

object MessageParametersEmpty : MessageParameters {
    override fun get(name: CharSequence): Any? {
        return null
    }
}