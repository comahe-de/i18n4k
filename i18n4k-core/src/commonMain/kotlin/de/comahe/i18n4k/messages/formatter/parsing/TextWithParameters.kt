package de.comahe.i18n4k.messages.formatter.parsing

interface TextWithParameters {
    fun fillInParameterNames(names: MutableSet<CharSequence>)
}