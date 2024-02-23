package de.comahe.i18n4k.messages

/** For message strings that only contain numbers as parameter names */
class NameToIndexMapperList(private vararg val  list: CharSequence) : NameToIndexMapper {
    override fun getNameIndex(name: CharSequence): Int {
        return list.indexOf(name)
    }
}