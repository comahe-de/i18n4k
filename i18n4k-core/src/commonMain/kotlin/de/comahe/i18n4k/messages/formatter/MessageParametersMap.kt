package de.comahe.i18n4k.messages.formatter

class MessageParametersMap(
    private val parameters: Map<String,Any>,
) : MessageParameters {

    constructor(vararg parameters: Pair<String, Any>)
        : this(mapOf(*parameters))

    override operator fun get(name: CharSequence): Any? {
        if(name == "~")
            return null
        return parameters[name]
    }
}