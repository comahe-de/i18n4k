package de.comahe.i18n4k.messages

/** For message strings that only contain numbers as parameter names */
class NameToIndexMapperMap(private val map: Map<CharSequence, Int>) : NameToIndexMapper {

    constructor(vararg entries: Pair<CharSequence, Int>) : this(mapOf(*entries))

    override fun getNameIndex(name: CharSequence): Int {
        return map[name] ?: -1
    }
}