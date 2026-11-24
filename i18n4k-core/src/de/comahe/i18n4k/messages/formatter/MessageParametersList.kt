package de.comahe.i18n4k.messages.formatter

import de.comahe.i18n4k.messages.NameToIndexMapper

class MessageParametersList(
    private val parameters: List<Any>,
    private val nameMapper: NameToIndexMapper,
) : MessageParameters {
    override operator fun get(name: CharSequence): Any? {
        if(name == "~")
            return null
        val index = nameMapper.getNameIndex(name)
        if (index < 0 || index >= parameters.size)
            return null
        return parameters[index]
    }
}