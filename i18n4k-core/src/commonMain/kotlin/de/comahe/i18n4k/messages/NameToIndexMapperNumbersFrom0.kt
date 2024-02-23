package de.comahe.i18n4k.messages

/** For message strings that only contain numbers as parameter names starting at 0 */
object NameToIndexMapperNumbersFrom0 : NameToIndexMapper {
    override fun getNameIndex(name: CharSequence): Int {
        if (name.length != 1)
            throw IllegalArgumentException("One digit expected, but got: $name")
        val char0: Char = name[0]
        if (char0 < '0' || char0 > '9')
            throw IllegalArgumentException("Digit between '0' and '9' expected, but got: $name")
        return char0 - '0'
    }
}