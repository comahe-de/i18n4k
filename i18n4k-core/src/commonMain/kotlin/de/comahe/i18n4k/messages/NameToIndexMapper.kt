package de.comahe.i18n4k.messages

/**
 * Maps a parameter name in the message string to a parameter index of the LocalizedStringFactory*
 */
interface NameToIndexMapper {
    /** The index of the parameter. -1 if the name was not found. */
    fun getNameIndex(name: CharSequence): Int
}