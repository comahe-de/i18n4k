package de.comahe.i18n4k.messages

/** For message strings that only contain numbers as parameter names starting at 1 */
object NameToIndexMapperNumbersFrom1 : NameToIndexMapper {
    override fun getNameIndex(name: CharSequence): Int {
        if(name == "10")
            return 9;
        if (name.length != 1)
            throw IllegalArgumentException("One digit or '10' expected, but got: $name")
        val char0: Char = name[0]
        if (char0 < '1' || char0 > '9')
            throw IllegalArgumentException("Digit between '1' and '9' or '10' expected, but got: $name")
        return char0 - '1'
    }
}